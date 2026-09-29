package adaptivelearning.module.questions.service.impl;

import adaptivelearning.config.response.CommonException;
import adaptivelearning.config.response.ErrorCode;
import adaptivelearning.module.questions.dto.request.QuestionFilterRequest;
import adaptivelearning.module.questions.dto.request.QuestionRequest;
import adaptivelearning.module.questions.dto.response.QuestionResponse;
import adaptivelearning.module.questions.entity.Question;
import adaptivelearning.module.questions.repository.QuestionRepository;
import adaptivelearning.module.questions.service.QuestionService;
import adaptivelearning.shared.enums.ContentStatus;
import adaptivelearning.shared.enums.QuestionDifficulty;
import adaptivelearning.utils.SecurityUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link QuestionService}.
 *
 * <p>Status lifecycle: {@code DRAFT -> IN_REVIEW -> APPROVED -> PUBLISHED -> ARCHIVED}.
 * Only {@code APPROVED} questions can be used in exams.</p>
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private static final String MULTIPLE_CHOICE = "MULTIPLE_CHOICE";
    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> getAll(QuestionFilterRequest filter) {
        QuestionFilterRequest safeFilter =
                filter == null ? QuestionFilterRequest.builder().build() : filter;

        List<Question> questions = questionRepository.findAll().stream()
                .filter(q -> matchesCertificate(q, safeFilter.getCertificateId()))
                .filter(q -> matchesTopic(q, safeFilter.getTopicId()))
                .filter(q -> matchesQuestionType(q, safeFilter.getQuestionType()))
                .filter(q -> matchesDifficulty(q, safeFilter.getDifficulty()))
                .filter(q -> matchesStatus(q, safeFilter.getStatus()))
                .filter(q -> matchesTag(q, safeFilter.getTag()))
                .collect(Collectors.toList());

        return questions.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponse getById(Long questionId) {
        Question question = findQuestionOrThrow(questionId);
        return toResponse(question);
    }

    @Override
    public QuestionResponse create(QuestionRequest request) {
        validateQuestion(request);

        Question question = Question.builder()
                .certificateId(request.getCertificateId())
                .topicId(request.getTopicId())
                .questionType(request.getQuestionType())
                .difficulty(request.getDifficulty() == null
                        ? QuestionDifficulty.MEDIUM
                        : request.getDifficulty())
                .content(request.getContent())
                .contentJson(request.getContentJson())
                .optionsJson(request.getOptionsJson())
                .correctAnswer(request.getCorrectAnswer())
                .explanation(request.getExplanation())
                .points(request.getPoints() == null ? 1.0 : request.getPoints())
                .estimatedTimeSeconds(
                        request.getEstimatedTimeSeconds() == null ? 0 : request.getEstimatedTimeSeconds())
                .tags(request.getTags())
                .status(ContentStatus.DRAFT)
                .version(1)
                .createdBy(SecurityUtils.getCurrentUsername())
                .build();

        Question saved = questionRepository.save(question);
        log.info("Created question {} for certificate {}", saved.getQuestionId(), saved.getCertificateId());
        return toResponse(saved);
    }

    @Override
    public QuestionResponse update(Long questionId, QuestionRequest request) {
        validateQuestion(request);

        Question question = findQuestionOrThrow(questionId);
        if (question.getStatus() == ContentStatus.ARCHIVED) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }

        question.setCertificateId(request.getCertificateId());
        question.setTopicId(request.getTopicId());
        question.setQuestionType(request.getQuestionType());
        if (request.getDifficulty() != null) {
            question.setDifficulty(request.getDifficulty());
        }
        question.setContent(request.getContent());
        question.setContentJson(request.getContentJson());
        question.setOptionsJson(request.getOptionsJson());
        question.setCorrectAnswer(request.getCorrectAnswer());
        question.setExplanation(request.getExplanation());
        if (request.getPoints() != null) {
            question.setPoints(request.getPoints());
        }
        if (request.getEstimatedTimeSeconds() != null) {
            question.setEstimatedTimeSeconds(request.getEstimatedTimeSeconds());
        }
        question.setTags(request.getTags());

        // Editing invalidates a prior approval; the question must be re-reviewed.
        if (question.getStatus() == ContentStatus.APPROVED
                || question.getStatus() == ContentStatus.PUBLISHED) {
            question.setStatus(ContentStatus.DRAFT);
        }
        question.setVersion(question.getVersion() + 1);

        Question saved = questionRepository.save(question);
        log.info("Updated question {}", saved.getQuestionId());
        return toResponse(saved);
    }

    @Override
    public QuestionResponse submitForReview(Long questionId) {
        Question question = findQuestionOrThrow(questionId);
        if (question.getStatus() != ContentStatus.DRAFT) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
        question.setStatus(ContentStatus.IN_REVIEW);
        return toResponse(questionRepository.save(question));
    }

    @Override
    public QuestionResponse approve(Long questionId) {
        Question question = findQuestionOrThrow(questionId);
        if (question.getStatus() != ContentStatus.IN_REVIEW) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
        question.setStatus(ContentStatus.APPROVED);
        return toResponse(questionRepository.save(question));
    }

    @Override
    public QuestionResponse reject(Long questionId) {
        Question question = findQuestionOrThrow(questionId);
        if (question.getStatus() != ContentStatus.IN_REVIEW) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
        question.setStatus(ContentStatus.DRAFT);
        return toResponse(questionRepository.save(question));
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> getRandomQuestions(
            Long certificateId, int count, Long topicId, QuestionDifficulty difficulty) {
        if (certificateId == null) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
        if (count <= 0) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }

        // Only APPROVED questions can be used in exams.
        List<Question> approved =
                questionRepository.findByCertificateIdAndStatus(certificateId, ContentStatus.APPROVED);

        List<Question> eligible = approved.stream()
                .filter(q -> topicId == null || topicId.equals(q.getTopicId()))
                .filter(q -> difficulty == null || difficulty == q.getDifficulty())
                .collect(Collectors.toList());

        Collections.shuffle(eligible);
        return eligible.stream()
                .limit(count)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countByCertificate(Long certificateId) {
        if (certificateId == null) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
        return questionRepository.countByCertificateIdAndStatus(certificateId, ContentStatus.APPROVED);
    }

    /**
     * Validates question content and MCQ option structure.
     */
    private void validateQuestion(QuestionRequest request) {
        if (request == null) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
        if (MULTIPLE_CHOICE.equalsIgnoreCase(request.getQuestionType())) {
            validateMcqOptions(request.getOptionsJson());
        }
    }

    /**
     * Ensures MCQ options are a non-empty JSON array of objects with non-empty label and text.
     */
    private void validateMcqOptions(String optionsJson) {
        if (optionsJson == null || optionsJson.trim().isEmpty()) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
        try {
            List<Map<String, Object>> options =
                    objectMapper.readValue(optionsJson, new TypeReference<List<Map<String, Object>>>() {});
            if (options.isEmpty()) {
                throw new CommonException(ErrorCode.BAD_REQUEST);
            }
            for (Map<String, Object> option : options) {
                Object label = option.get("label");
                Object text = option.get("text");
                if (label == null || label.toString().trim().isEmpty()
                        || text == null || text.toString().trim().isEmpty()) {
                    throw new CommonException(ErrorCode.BAD_REQUEST);
                }
            }
        } catch (CommonException e) {
            throw e;
        } catch (Exception e) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
    }

    private Question findQuestionOrThrow(Long questionId) {
        if (questionId == null) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new CommonException(ErrorCode.ENTITY_NOT_FOUND));
    }

    private QuestionResponse toResponse(Question question) {
        if (question == null) {
            return null;
        }
        return QuestionResponse.builder()
                .questionId(question.getQuestionId())
                .certificateId(question.getCertificateId())
                .topicId(question.getTopicId())
                .questionType(question.getQuestionType())
                .difficulty(question.getDifficulty())
                .content(question.getContent())
                .contentJson(question.getContentJson())
                .optionsJson(question.getOptionsJson())
                .correctAnswer(question.getCorrectAnswer())
                .explanation(question.getExplanation())
                .points(question.getPoints())
                .estimatedTimeSeconds(question.getEstimatedTimeSeconds())
                .tags(question.getTags())
                .status(question.getStatus())
                .version(question.getVersion())
                .createdBy(question.getCreatedBy())
                .createdAt(question.getCreatedAt())
                .updatedId(question.getUpdatedId())
                .updatedAt(question.getUpdatedAt())
                .build();
    }

    private boolean matchesCertificate(Question q, Long certificateId) {
        return certificateId == null || certificateId.equals(q.getCertificateId());
    }

    private boolean matchesTopic(Question q, Long topicId) {
        return topicId == null || topicId.equals(q.getTopicId());
    }

    private boolean matchesQuestionType(Question q, String questionType) {
        return questionType == null || questionType.trim().isEmpty()
                || questionType.equalsIgnoreCase(q.getQuestionType());
    }

    private boolean matchesDifficulty(Question q, QuestionDifficulty difficulty) {
        return difficulty == null || difficulty == q.getDifficulty();
    }

    private boolean matchesStatus(Question q, ContentStatus status) {
        return status == null || status == q.getStatus();
    }

    private boolean matchesTag(Question q, String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            return true;
        }
        if (q.getTags() == null || q.getTags().trim().isEmpty()) {
            return false;
        }
        String normalized = tag.trim().toLowerCase();
        for (String existing : q.getTags().split(",")) {
            if (existing.trim().toLowerCase().equals(normalized)) {
                return true;
            }
        }
        return false;
    }
}
