package adaptivelearning.module.attempts.service.impl;

import adaptivelearning.config.response.CommonException;
import adaptivelearning.config.response.ErrorCode;
import adaptivelearning.module.attempts.dto.request.SaveAnswerRequest;
import adaptivelearning.module.attempts.dto.request.SubmitAttemptRequest;
import adaptivelearning.module.attempts.dto.request.ViolationRequest;
import adaptivelearning.module.attempts.dto.response.AttemptAnswerResponse;
import adaptivelearning.module.attempts.dto.response.AttemptDetailResponse;
import adaptivelearning.module.attempts.dto.response.AttemptResponse;
import adaptivelearning.module.attempts.dto.response.AttemptResultResponse;
import adaptivelearning.module.attempts.entity.AttemptAnswer;
import adaptivelearning.module.attempts.entity.AttemptEvent;
import adaptivelearning.module.attempts.entity.ExamAttempt;
import adaptivelearning.module.attempts.repository.AttemptAnswerRepository;
import adaptivelearning.module.attempts.repository.AttemptEventRepository;
import adaptivelearning.module.attempts.repository.ExamAttemptRepository;
import adaptivelearning.module.attempts.service.AttemptService;
import adaptivelearning.module.exams.entity.Exam;
import adaptivelearning.module.exams.entity.ExamQuestion;
import adaptivelearning.module.exams.repository.ExamQuestionRepository;
import adaptivelearning.module.exams.repository.ExamRepository;
import adaptivelearning.module.questions.entity.Question;
import adaptivelearning.module.questions.repository.QuestionRepository;
import adaptivelearning.shared.enums.AttemptStatus;
import adaptivelearning.shared.enums.ContentStatus;
import adaptivelearning.shared.enums.EventType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AttemptServiceImpl implements AttemptService {

    private final ExamAttemptRepository attemptRepository;
    private final AttemptAnswerRepository answerRepository;
    private final AttemptEventRepository eventRepository;
    private final ExamRepository examRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;

    @Override
    public AttemptResponse startAttempt(Long userId, Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new CommonException(ErrorCode.ENTITY_NOT_FOUND));
        if (exam.getStatus() != ContentStatus.PUBLISHED) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
        attemptRepository.findByUserIdAndExamId(userId, examId).ifPresent(existing -> {
            if (existing.getStatus() == AttemptStatus.IN_PROGRESS) {
                throw new CommonException(ErrorCode.BAD_REQUEST);
            }
        });

        LocalDateTime startedAt = LocalDateTime.now();
        ExamAttempt attempt = ExamAttempt.builder()
                .userId(userId)
                .examId(examId)
                .status(AttemptStatus.IN_PROGRESS)
                .startedAt(startedAt)
                .expiresAt(startedAt.plusMinutes(exam.getDurationMinutes()))
                .maxScore(exam.getTotalPoints())
                .build();

        ExamAttempt saved = attemptRepository.save(attempt);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AttemptDetailResponse getAttempt(Long attemptId) {
        ExamAttempt attempt = requireAttempt(attemptId);
        List<AttemptAnswerResponse> answers = answerRepository.findByAttemptId(attemptId).stream()
                .map(this::toAnswerResponse).toList();

        return AttemptDetailResponse.builder()
                .attemptId(attempt.getAttemptId())
                .userId(attempt.getUserId())
                .examId(attempt.getExamId())
                .status(attempt.getStatus())
                .startedAt(attempt.getStartedAt())
                .expiresAt(attempt.getExpiresAt())
                .submittedAt(attempt.getSubmittedAt())
                .score(attempt.getScore())
                .maxScore(attempt.getMaxScore())
                .bandScore(attempt.getBandScore())
                .scoreBreakdownJson(attempt.getScoreBreakdownJson())
                .violationCount(attempt.getViolationCount())
                .ipAddress(attempt.getIpAddress())
                .userAgent(attempt.getUserAgent())
                .answers(answers)
                .timeRemainingSeconds(timeRemainingSeconds(attempt))
                .build();
    }

    @Override
    public void saveAnswer(Long attemptId, Long userId, SaveAnswerRequest request) {
        ExamAttempt attempt = requireOwnedAttempt(attemptId, userId);
        ensureActive(attempt);

        AttemptAnswer answer = answerRepository.findByAttemptIdAndQuestionId(attemptId, request.getQuestionId())
                .orElseGet(() -> AttemptAnswer.builder()
                        .attemptId(attemptId)
                        .questionId(request.getQuestionId())
                        .build());

        answer.setUserAnswerJson(request.getUserAnswerJson());
        answer.setTimeSpentSeconds(request.getTimeSpentSeconds());
        answerRepository.save(answer);

        eventRepository.save(AttemptEvent.builder()
                .attemptId(attemptId)
                .eventType(EventType.AUTOSAVE)
                .build());
    }

    @Override
    public AttemptResultResponse submitAttempt(Long attemptId, Long userId, SubmitAttemptRequest request) {
        ExamAttempt attempt = requireOwnedAttempt(attemptId, userId);
        ensureActive(attempt);

        if (request.getAnswers() != null) {
            for (SaveAnswerRequest ans : request.getAnswers()) {
                saveAnswer(attemptId, userId, ans);
            }
        }

        attempt.setSubmittedAt(LocalDateTime.now());
        attempt.setStatus(AttemptStatus.SUBMITTED);
        eventRepository.save(AttemptEvent.builder()
                .attemptId(attemptId)
                .eventType(EventType.SUBMIT)
                .build());

        return scoreAttempt(attempt);
    }

    @Override
    @Transactional(readOnly = true)
    public AttemptResultResponse getResult(Long attemptId) {
        ExamAttempt attempt = requireAttempt(attemptId);
        if (attempt.getScore() == null) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
        return buildResultResponse(attempt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttemptResponse> getHistory(Long userId) {
        return attemptRepository.findByUserIdOrderByStartedAtDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void recordViolation(Long attemptId, ViolationRequest request) {
        ExamAttempt attempt = requireAttempt(attemptId);
        ensureActive(attempt);

        attempt.setViolationCount(attempt.getViolationCount() + 1);

        eventRepository.save(AttemptEvent.builder()
                .attemptId(attemptId)
                .eventType(request.getEventType() != null ? request.getEventType() : EventType.VIOLATION)
                .payloadJson(request.getPayloadJson())
                .build());

        if (attempt.getViolationCount() >= 5) {
            attempt.setSubmittedAt(LocalDateTime.now());
            attempt.setStatus(AttemptStatus.VIOLATED);
            scoreAttempt(attempt);
        } else {
            attemptRepository.save(attempt);
        }
    }

    @Override
    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void expireOverdueAttempts() {
        LocalDateTime now = LocalDateTime.now();
        List<ExamAttempt> inProgress = attemptRepository.findByStatus(AttemptStatus.IN_PROGRESS);
        List<ExamAttempt> overdue = inProgress.stream()
                .filter(a -> a.getExpiresAt() != null && a.getExpiresAt().isBefore(now))
                .toList();
        for (ExamAttempt attempt : overdue) {
            attempt.setStatus(AttemptStatus.EXPIRED);
        }
        attemptRepository.saveAll(overdue);
    }

    // ---------------------------------------------------------------------
    // Scoring
    // ---------------------------------------------------------------------

    private AttemptResultResponse scoreAttempt(ExamAttempt attempt) {
        attempt.setStatus(AttemptStatus.GRADING);
        attemptRepository.save(attempt);

        List<ExamQuestion> examQuestions = examQuestionRepository.findByExamId(attempt.getExamId());
        List<Long> questionIds = examQuestions.stream().map(ExamQuestion::getQuestionId).toList();
        Map<Long, Question> questionMap = questionIds.isEmpty()
                ? Map.of()
                : questionRepository.findAllById(questionIds).stream()
                        .collect(Collectors.toMap(Question::getQuestionId, Function.identity()));

        List<AttemptAnswer> answers = answerRepository.findByAttemptId(attempt.getAttemptId());
        double score = 0d;
        int correctCount = 0;

        for (AttemptAnswer answer : answers) {
            Question question = questionMap.get(answer.getQuestionId());
            if (question == null) {
                answer.setIsCorrect(null);
                continue;
            }

            if (question.getCorrectAnswer() == null) {
                answer.setIsCorrect(null);
                continue;
            }

            boolean correct = isCorrect(answer.getUserAnswerJson(), question.getCorrectAnswer(), question.getQuestionType());
            answer.setIsCorrect(correct);

            if (correct) {
                answer.setPointsEarned(question.getPoints());
                score += question.getPoints();
                correctCount++;
            } else {
                answer.setPointsEarned(0d);
            }
        }

        answerRepository.saveAll(answers);

        attempt.setScore(score);
        attempt.setStatus(AttemptStatus.GRADED);
        attemptRepository.save(attempt);

        return buildResultResponse(attempt, examQuestions.size(), correctCount, answers);
    }

    private boolean isCorrect(String userAnswerJson, String correctAnswer, String questionType) {
        if (userAnswerJson == null || correctAnswer == null) {
            return false;
        }
        try {
            JsonNode userNode = objectMapper.readTree(userAnswerJson);
            JsonNode correctNode = objectMapper.readTree(correctAnswer);

            if ("ESSAY".equalsIgnoreCase(questionType)) {
                return false;
            }
            return userNode.equals(correctNode);
        } catch (Exception e) {
            return userAnswerJson.trim().equalsIgnoreCase(correctAnswer.trim());
        }
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private ExamAttempt requireAttempt(Long id) {
        return attemptRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.ENTITY_NOT_FOUND));
    }

    private ExamAttempt requireOwnedAttempt(Long id, Long userId) {
        ExamAttempt attempt = requireAttempt(id);
        if (!Objects.equals(attempt.getUserId(), userId)) {
            throw new CommonException(ErrorCode.FORBIDDEN_ERROR);
        }
        return attempt;
    }

    private void ensureActive(ExamAttempt attempt) {
        if (attempt.getStatus() != AttemptStatus.IN_PROGRESS) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
        if (attempt.getExpiresAt() != null && attempt.getExpiresAt().isBefore(LocalDateTime.now())) {
            attempt.setStatus(AttemptStatus.EXPIRED);
            attemptRepository.save(attempt);
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
    }

    private long timeRemainingSeconds(ExamAttempt attempt) {
        if (attempt.getExpiresAt() == null || attempt.getStatus() != AttemptStatus.IN_PROGRESS) {
            return 0;
        }
        return Math.max(0, Duration.between(LocalDateTime.now(), attempt.getExpiresAt()).getSeconds());
    }

    private AttemptResponse toResponse(ExamAttempt attempt) {
        return AttemptResponse.builder()
                .attemptId(attempt.getAttemptId())
                .userId(attempt.getUserId())
                .examId(attempt.getExamId())
                .status(attempt.getStatus())
                .startedAt(attempt.getStartedAt())
                .expiresAt(attempt.getExpiresAt())
                .submittedAt(attempt.getSubmittedAt())
                .score(attempt.getScore())
                .maxScore(attempt.getMaxScore())
                .bandScore(attempt.getBandScore())
                .scoreBreakdownJson(attempt.getScoreBreakdownJson())
                .violationCount(attempt.getViolationCount())
                .ipAddress(attempt.getIpAddress())
                .userAgent(attempt.getUserAgent())
                .build();
    }

    private AttemptAnswerResponse toAnswerResponse(AttemptAnswer answer) {
        return AttemptAnswerResponse.builder()
                .id(answer.getId())
                .questionId(answer.getQuestionId())
                .userAnswerJson(answer.getUserAnswerJson())
                .isCorrect(answer.getIsCorrect())
                .pointsEarned(answer.getPointsEarned())
                .timeSpentSeconds(answer.getTimeSpentSeconds())
                .orderAnswered(answer.getOrderAnswered())
                .build();
    }

    private AttemptResultResponse buildResultResponse(ExamAttempt attempt) {
        List<AttemptAnswer> answers = answerRepository.findByAttemptId(attempt.getAttemptId());
        long correct = answerRepository.countByAttemptIdAndIsCorrect(attempt.getAttemptId(), true);
        long wrong = answers.stream().filter(a -> Boolean.FALSE.equals(a.getIsCorrect())).count();
        int total = examQuestionRepository.countByExamId(attempt.getExamId());

        return buildResultResponse(attempt, total, (int) correct, answers);
    }

    private AttemptResultResponse buildResultResponse(ExamAttempt attempt, int totalQuestions,
                                                      int correctCount, List<AttemptAnswer> answers) {
        long wrongCount = answers.stream().filter(a -> Boolean.FALSE.equals(a.getIsCorrect())).count();
        int unanswered = totalQuestions - answers.size();

        long timeTaken = 0;
        if (attempt.getStartedAt() != null && attempt.getSubmittedAt() != null) {
            timeTaken = Duration.between(attempt.getStartedAt(), attempt.getSubmittedAt()).getSeconds();
        }

        return AttemptResultResponse.builder()
                .attemptId(attempt.getAttemptId())
                .score(attempt.getScore())
                .maxScore(attempt.getMaxScore())
                .bandScore(attempt.getBandScore())
                .totalQuestions(totalQuestions)
                .correctCount(correctCount)
                .wrongCount((int) wrongCount)
                .unansweredCount(Math.max(0, unanswered))
                .scoreBreakdownJson(attempt.getScoreBreakdownJson())
                .timeTakenSeconds(Math.max(0, timeTaken))
                .violationCount(attempt.getViolationCount())
                .build();
    }
}
