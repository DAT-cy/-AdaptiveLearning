package adaptivelearning.module.exams.service.impl;

import adaptivelearning.config.response.CommonException;
import adaptivelearning.config.response.ErrorCode;
import adaptivelearning.module.exams.dto.request.ExamQuestionRequest;
import adaptivelearning.module.exams.dto.request.ExamRequest;
import adaptivelearning.module.exams.dto.response.ExamDetailResponse;
import adaptivelearning.module.exams.dto.response.ExamQuestionResponse;
import adaptivelearning.module.exams.dto.response.ExamResponse;
import adaptivelearning.module.exams.entity.Exam;
import adaptivelearning.module.exams.entity.ExamQuestion;
import adaptivelearning.module.exams.repository.ExamQuestionRepository;
import adaptivelearning.module.exams.repository.ExamRepository;
import adaptivelearning.module.exams.service.ExamService;
import adaptivelearning.module.questions.entity.Question;
import adaptivelearning.module.questions.repository.QuestionRepository;
import adaptivelearning.shared.enums.ContentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Business logic for exam lifecycle management, question assembly, and totals.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final QuestionRepository questionRepository;

    // ---------------------------------------------------------------------
    // Queries
    // ---------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getAll(Long certificateId) {
        List<Exam> exams = (certificateId == null)
                ? examRepository.findAll()
                : examRepository.findByCertificateIdAndStatus(certificateId, ContentStatus.PUBLISHED);

        return exams.stream().map(exam -> toResponse(exam, examQuestionRepository.countByExamId(exam.getExamId()))).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ExamResponse getById(Long id) {
        Exam exam = requireExam(id);
        return toResponse(exam, examQuestionRepository.countByExamId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ExamDetailResponse getDetail(Long id) {
        Exam exam = requireExam(id);
        List<ExamQuestion> questions = examQuestionRepository.findByExamId(id);

        return toDetailResponse(exam, questions);
    }

    // ---------------------------------------------------------------------
    // Create / Update / Delete
    // ---------------------------------------------------------------------

    @Override
    public ExamResponse create(ExamRequest request) {
        Exam exam = Exam.builder()
                .certificateId(request.getCertificateId())
                .title(request.getTitle())
                .description(request.getDescription())
                .durationMinutes(request.getDurationMinutes())
                .passingScore(request.getPassingScore())
                .blueprintJson(request.getBlueprintJson())
                .scoringJson(request.getScoringJson())
                .status(ContentStatus.DRAFT)
                .build();

        Exam saved = examRepository.save(exam);
        return toResponse(saved, 0);
    }

    @Override
    public ExamResponse update(Long id, ExamRequest request) {
        Exam exam = requireExam(id);

        if (exam.getStatus() == ContentStatus.PUBLISHED || exam.getStatus() == ContentStatus.ARCHIVED) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }

        exam.setCertificateId(request.getCertificateId());
        exam.setTitle(request.getTitle());
        exam.setDescription(request.getDescription());
        exam.setDurationMinutes(request.getDurationMinutes());
        exam.setPassingScore(request.getPassingScore());
        exam.setBlueprintJson(request.getBlueprintJson());
        exam.setScoringJson(request.getScoringJson());
        exam.setVersion(exam.getVersion() + 1);

        Exam saved = examRepository.save(exam);
        return toResponse(saved, examQuestionRepository.countByExamId(id));
    }

    @Override
    public void delete(Long id) {
        Exam exam = requireExam(id);

        if (exam.getStatus() == ContentStatus.PUBLISHED) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }

        examQuestionRepository.deleteByExamId(id);
        examRepository.delete(exam);
    }

    // ---------------------------------------------------------------------
    // Status transitions: DRAFT -> PUBLISHED -> ARCHIVED
    // ---------------------------------------------------------------------

    @Override
    public ExamResponse publish(Long id) {
        Exam exam = requireExam(id);

        if (exam.getStatus() != ContentStatus.DRAFT) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }

        int count = examQuestionRepository.countByExamId(id);
        if (count <= 0) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }

        exam.setStatus(ContentStatus.PUBLISHED);
        Exam saved = examRepository.save(exam);
        return toResponse(saved, count);
    }

    @Override
    public ExamResponse archive(Long id) {
        Exam exam = requireExam(id);

        if (exam.getStatus() != ContentStatus.PUBLISHED) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }

        exam.setStatus(ContentStatus.ARCHIVED);
        Exam saved = examRepository.save(exam);
        return toResponse(saved, examQuestionRepository.countByExamId(id));
    }

    // ---------------------------------------------------------------------
    // Question management
    // ---------------------------------------------------------------------

    @Override
    public ExamQuestionResponse addQuestion(Long examId, ExamQuestionRequest request) {
        requireExam(examId);

        if (request.getQuestionId() == null) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }

        if (examQuestionRepository.findByExamIdAndQuestionId(examId, request.getQuestionId()).isPresent()) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }

        ExamQuestion examQuestion = ExamQuestion.builder()
                .examId(examId)
                .questionId(request.getQuestionId())
                .questionOrder(request.getQuestionOrder())
                .pointsOverride(request.getPointsOverride())
                .sectionName(request.getSectionName())
                .build();

        ExamQuestion saved = examQuestionRepository.save(examQuestion);
        recalculateTotals(examId);

        return toQuestionResponse(saved);
    }

    @Override
    public void removeQuestion(Long examId, Long questionId) {
        requireExam(examId);

        ExamQuestion examQuestion = examQuestionRepository.findByExamIdAndQuestionId(examId, questionId)
                .orElseThrow(() -> new CommonException(ErrorCode.ENTITY_NOT_FOUND));

        examQuestionRepository.delete(examQuestion);
        recalculateTotals(examId);
    }

    @Override
    public List<ExamQuestionResponse> reorderQuestions(Long examId, List<ExamQuestionRequest> requests) {
        requireExam(examId);

        Map<Long, ExamQuestion> byQuestionId = examQuestionRepository.findByExamId(examId).stream()
                .collect(Collectors.toMap(ExamQuestion::getQuestionId, Function.identity()));

        List<ExamQuestion> updated = new ArrayList<>();

        for (ExamQuestionRequest request : requests) {
            ExamQuestion existing = byQuestionId.get(request.getQuestionId());
            if (existing == null) {
                throw new CommonException(ErrorCode.ENTITY_NOT_FOUND);
            }

            existing.setQuestionOrder(request.getQuestionOrder());
            if (request.getSectionName() != null) {
                existing.setSectionName(request.getSectionName());
            }
            if (request.getPointsOverride() != null) {
                existing.setPointsOverride(request.getPointsOverride());
            }
            updated.add(existing);
        }

        examQuestionRepository.saveAll(updated);
        recalculateTotals(examId);

        return updated.stream().map(this::toQuestionResponse).toList();
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private Exam requireExam(Long id) {
        return examRepository.findById(id).orElseThrow(() -> new CommonException(ErrorCode.ENTITY_NOT_FOUND));
    }

    /**
     * Recalculates totalQuestions and totalPoints on the parent exam.
     * Points come from pointsOverride when present, otherwise the question's default points.
     */
    private void recalculateTotals(Long examId) {
        List<ExamQuestion> examQuestions = examQuestionRepository.findByExamId(examId);

        List<Long> questionIds = examQuestions.stream().map(ExamQuestion::getQuestionId).toList();
        Map<Long, Question> questionsById = questionIds.isEmpty()
                ? Map.of()
                : questionRepository.findAllById(questionIds).stream()
                        .collect(Collectors.toMap(Question::getQuestionId, Function.identity()));

        double totalPoints = 0d;
        for (ExamQuestion eq : examQuestions) {
            if (eq.getPointsOverride() != null) {
                totalPoints += eq.getPointsOverride();
            } else {
                Question question = questionsById.get(eq.getQuestionId());
                totalPoints += (question != null) ? question.getPoints() : 0d;
            }
        }

        Exam exam = requireExam(examId);
        exam.setTotalQuestions(examQuestions.size());
        exam.setTotalPoints(totalPoints);
        examRepository.save(exam);
    }

    private ExamResponse toResponse(Exam exam, int questionCount) {
        return ExamResponse.builder()
                .examId(exam.getExamId())
                .certificateId(exam.getCertificateId())
                .title(exam.getTitle())
                .description(exam.getDescription())
                .durationMinutes(exam.getDurationMinutes())
                .totalQuestions(exam.getTotalQuestions())
                .totalPoints(exam.getTotalPoints())
                .passingScore(exam.getPassingScore())
                .status(exam.getStatus())
                .blueprintJson(exam.getBlueprintJson())
                .scoringJson(exam.getScoringJson())
                .version(exam.getVersion())
                .questionCount(questionCount)
                .createdAt(exam.getCreatedAt())
                .updatedAt(exam.getUpdatedAt())
                .build();
    }

    private ExamDetailResponse toDetailResponse(Exam exam, List<ExamQuestion> examQuestions) {
        ExamResponse base = toResponse(exam, examQuestions.size());

        return ExamDetailResponse.builder()
                .examId(base.getExamId())
                .certificateId(base.getCertificateId())
                .title(base.getTitle())
                .description(base.getDescription())
                .durationMinutes(base.getDurationMinutes())
                .totalQuestions(base.getTotalQuestions())
                .totalPoints(base.getTotalPoints())
                .passingScore(base.getPassingScore())
                .status(base.getStatus())
                .blueprintJson(base.getBlueprintJson())
                .scoringJson(base.getScoringJson())
                .version(base.getVersion())
                .questionCount(base.getQuestionCount())
                .createdAt(base.getCreatedAt())
                .updatedAt(base.getUpdatedAt())
                .questions(examQuestions.stream().map(this::toQuestionResponse).toList())
                .build();
    }

    private ExamQuestionResponse toQuestionResponse(ExamQuestion examQuestion) {
        return ExamQuestionResponse.builder()
                .id(examQuestion.getId())
                .examId(examQuestion.getExamId())
                .questionId(examQuestion.getQuestionId())
                .questionOrder(examQuestion.getQuestionOrder())
                .pointsOverride(examQuestion.getPointsOverride())
                .sectionName(examQuestion.getSectionName())
                .build();
    }
}
