package adaptivelearning.module.ai.service.impl;

import adaptivelearning.config.response.CommonException;
import adaptivelearning.config.response.ErrorCode;
import adaptivelearning.module.ai.dto.request.AiGradeRequest;
import adaptivelearning.module.ai.dto.request.RubricRequest;
import adaptivelearning.module.ai.dto.response.AiEvaluationResponse;
import adaptivelearning.module.ai.dto.response.AiJobResponse;
import adaptivelearning.module.ai.dto.response.RubricResponse;
import adaptivelearning.module.ai.entity.AiEvaluation;
import adaptivelearning.module.ai.entity.AiJob;
import adaptivelearning.module.ai.entity.Rubric;
import adaptivelearning.module.ai.gateway.AiProvider;
import adaptivelearning.module.ai.repository.AiEvaluationRepository;
import adaptivelearning.module.ai.repository.AiJobRepository;
import adaptivelearning.module.ai.repository.RubricRepository;
import adaptivelearning.module.ai.service.AiService;
import adaptivelearning.shared.enums.AiJobStatus;
import adaptivelearning.shared.enums.AiJobType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AiServiceImpl implements AiService {

    private static final String DEFAULT_MODEL = "gpt-4o-mini";
    private static final String PROMPT_VERSION = "v1.0";

    private final AiEvaluationRepository evaluationRepository;
    private final AiJobRepository jobRepository;
    private final RubricRepository rubricRepository;
    private final AiProvider aiProvider;
    private final ObjectMapper objectMapper;

    @Override
    public AiEvaluationResponse gradeWriting(AiGradeRequest request) {
        validateGradeRequest(request);

        Rubric rubric = null;
        if (request.getRubricId() != null) {
            rubric = findRubric(request.getRubricId());
        }

        String systemPrompt = buildWritingPrompt(rubric);
        String userPrompt = "Grade the following answer and return only valid JSON:\n\n"
                + request.getUserAnswerText();

        String raw = aiProvider.chatCompletionSync(systemPrompt, userPrompt, DEFAULT_MODEL, 1500);

        AiEvaluation evaluation = evaluationRepository
                .findByAttemptIdAndQuestionId(request.getAttemptId(), request.getQuestionId())
                .orElseGet(AiEvaluation::new);

        evaluation.setAttemptId(request.getAttemptId());
        evaluation.setQuestionId(request.getQuestionId());
        evaluation.setRubricId(request.getRubricId());
        evaluation.setUserAnswerText(request.getUserAnswerText());
        evaluation.setModel(DEFAULT_MODEL);
        evaluation.setPromptVersion(PROMPT_VERSION);
        evaluation.setStatus(AiJobStatus.SUCCEEDED);
        evaluation.setRawResponseId(UUID.randomUUID().toString());

        parseEvaluationResponse(raw, evaluation);

        AiEvaluation saved = evaluationRepository.save(evaluation);
        log.info("Graded attempt {} question {}, score={}", saved.getAttemptId(), saved.getQuestionId(), saved.getOverallScore());
        return toEvaluationResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AiEvaluationResponse getEvaluation(Long evaluationId) {
        AiEvaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new CommonException(ErrorCode.ENTITY_NOT_FOUND));
        return toEvaluationResponse(evaluation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AiEvaluationResponse> getEvaluationByAttempt(Long attemptId) {
        return evaluationRepository.findByAttemptId(attemptId).stream()
                .map(this::toEvaluationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RubricResponse createRubric(RubricRequest request) {
        Rubric rubric = Rubric.builder()
                .certificateId(request.getCertificateId())
                .skill(request.getSkill())
                .name(request.getName())
                .description(request.getDescription())
                .criteriaJson(request.getCriteriaJson())
                .build();
        Rubric saved = rubricRepository.save(rubric);
        log.info("Created rubric {} for certificate {}", saved.getRubricId(), saved.getCertificateId());
        return toRubricResponse(saved);
    }

    @Override
    public RubricResponse updateRubric(Long rubricId, RubricRequest request) {
        Rubric rubric = findRubric(rubricId);
        rubric.setCertificateId(request.getCertificateId());
        rubric.setSkill(request.getSkill());
        rubric.setName(request.getName());
        rubric.setDescription(request.getDescription());
        rubric.setCriteriaJson(request.getCriteriaJson());
        rubric.setVersion(rubric.getVersion() + 1);
        Rubric saved = rubricRepository.save(rubric);
        log.info("Updated rubric {}", saved.getRubricId());
        return toRubricResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RubricResponse> getRubricsByCertificate(Long certificateId) {
        return rubricRepository.findByCertificateId(certificateId).stream()
                .map(this::toRubricResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AiJobResponse submitGradeJob(AiGradeRequest request) {
        validateGradeRequest(request);

        String idempotencyKey = "GRADE_WRITING:" + request.getAttemptId() + ":" + request.getQuestionId();

        AiJob job = jobRepository.findByIdempotencyKey(idempotencyKey).orElseGet(() ->
                jobRepository.save(AiJob.builder()
                        .jobType(AiJobType.GRADE_WRITING)
                        .status(AiJobStatus.PENDING)
                        .idempotencyKey(idempotencyKey)
                        .inputRefType("ATTEMPT_ANSWER")
                        .inputRefId(request.getAttemptId())
                        .model(DEFAULT_MODEL)
                        .promptVersion(PROMPT_VERSION)
                        .build()));

        evaluationRepository.findByAttemptIdAndQuestionId(request.getAttemptId(), request.getQuestionId())
                .orElseGet(() -> evaluationRepository.save(AiEvaluation.builder()
                        .attemptId(request.getAttemptId())
                        .questionId(request.getQuestionId())
                        .rubricId(request.getRubricId())
                        .userAnswerText(request.getUserAnswerText())
                        .status(AiJobStatus.PENDING)
                        .build()));

        if (job.getStatus() == AiJobStatus.PENDING) {
            processJobAsync(job, request);
        }

        return toJobResponse(job);
    }

    @Async
    @Override
    public void processPendingJobs() {
        List<AiJob> pendingJobs = jobRepository.findByStatus(AiJobStatus.PENDING);
        for (AiJob job : pendingJobs) {
            log.info("Processing pending job {}", job.getJobId());
            if (job.getJobType() != AiJobType.GRADE_WRITING) {
                continue;
            }
            evaluationRepository.findByAttemptId(job.getInputRefId()).stream()
                    .filter(evaluation -> evaluation.getStatus() == AiJobStatus.PENDING)
                    .findFirst()
                    .ifPresent(evaluation -> processJobAsync(job, AiGradeRequest.builder()
                            .attemptId(evaluation.getAttemptId())
                            .questionId(evaluation.getQuestionId())
                            .rubricId(evaluation.getRubricId())
                            .userAnswerText(evaluation.getUserAnswerText())
                            .build()));
        }
    }

    @Override
    public AiJobResponse retryJob(Long jobId) {
        AiJob job = jobRepository.findById(jobId)
                .orElseThrow(() -> new CommonException(ErrorCode.ENTITY_NOT_FOUND));
        if (job.getStatus() != AiJobStatus.FAILED) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
        job.setStatus(AiJobStatus.PENDING);
        job.setRetryCount(job.getRetryCount() + 1);
        job.setErrorMessage(null);
        AiJob saved = jobRepository.save(job);
        log.info("Retrying job {}", saved.getJobId());
        return toJobResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AiJobResponse> getJobs() {
        return jobRepository.findAll().stream()
                .map(this::toJobResponse)
                .collect(Collectors.toList());
    }

    private void processJobAsync(AiJob job, AiGradeRequest request) {
        job.setStatus(AiJobStatus.PROCESSING);
        job.setStartedAt(LocalDateTime.now());
        jobRepository.save(job);

        try {
            AiEvaluationResponse response = gradeWriting(request);
            job.setStatus(AiJobStatus.SUCCEEDED);
            job.setOutputRefType("AI_EVALUATION");
            job.setOutputRefId(response.getEvaluationId());
            job.setCompletedAt(LocalDateTime.now());
        } catch (RuntimeException ex) {
            job.setRetryCount(job.getRetryCount() + 1);
            job.setErrorMessage(ex.getMessage());
            job.setStatus(job.getRetryCount() < job.getMaxRetries()
                    ? AiJobStatus.RETRYING : AiJobStatus.FAILED);
            log.error("Job {} failed: {}", job.getJobId(), ex.getMessage());
        }

        jobRepository.save(job);
    }

    private String buildWritingPrompt(Rubric rubric) {
        String rubricSection = rubric != null
                ? "Use this rubric criteria: " + rubric.getCriteriaJson()
                : "Evaluate based on general writing quality criteria (grammar, vocabulary, coherence, task response).";
        return "You are an expert language examiner. Score the student's writing answer objectively. "
                + rubricSection + " "
                + "Return ONLY a valid JSON object with the following fields: "
                + "overallScore (double, 0-10), "
                + "criteria (array of {name, score, feedback}), "
                + "errors (array of {type, original, correction, explanation}), "
                + "strengths (array of strings), "
                + "rewriteSuggestion (string), "
                + "confidence (double, 0.0-1.0).";
    }

    private void parseEvaluationResponse(String raw, AiEvaluation evaluation) {
        try {
            JsonNode root = objectMapper.readTree(raw);

            if (root.has("overallScore") && root.get("overallScore").isNumber()) {
                evaluation.setOverallScore(root.get("overallScore").doubleValue());
            }

            evaluation.setCriteriaJson(extractField(root, "criteria"));
            evaluation.setErrorsJson(extractField(root, "errors"));
            evaluation.setStrengthsJson(extractField(root, "strengths"));

            if (root.has("rewriteSuggestion")) {
                evaluation.setRewriteSuggestion(root.get("rewriteSuggestion").asText(null));
            }

            if (root.has("confidence") && root.get("confidence").isNumber()) {
                evaluation.setConfidence(root.get("confidence").doubleValue());
            }
        } catch (Exception ex) {
            log.warn("Failed to parse AI evaluation JSON response: {}", ex.getMessage());
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
    }

    private String extractField(JsonNode root, String fieldName) {
        JsonNode node = root.get(fieldName);
        if (node == null || node.isNull()) {
            return null;
        }
        return node.toString();
    }

    private void validateGradeRequest(AiGradeRequest request) {
        if (request == null || request.getAttemptId() == null || request.getQuestionId() == null
                || request.getUserAnswerText() == null || request.getUserAnswerText().isBlank()) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
    }

    private Rubric findRubric(Long rubricId) {
        return rubricRepository.findById(rubricId)
                .orElseThrow(() -> new CommonException(ErrorCode.ENTITY_NOT_FOUND));
    }

    private AiEvaluationResponse toEvaluationResponse(AiEvaluation e) {
        return AiEvaluationResponse.builder()
                .evaluationId(e.getEvaluationId())
                .attemptId(e.getAttemptId())
                .questionId(e.getQuestionId())
                .rubricId(e.getRubricId())
                .userAnswerText(e.getUserAnswerText())
                .overallScore(e.getOverallScore())
                .criteriaJson(e.getCriteriaJson())
                .errorsJson(e.getErrorsJson())
                .strengthsJson(e.getStrengthsJson())
                .rewriteSuggestion(e.getRewriteSuggestion())
                .confidence(e.getConfidence())
                .model(e.getModel())
                .promptVersion(e.getPromptVersion())
                .rawResponseId(e.getRawResponseId())
                .status(e.getStatus())
                .build();
    }

    private AiJobResponse toJobResponse(AiJob j) {
        return AiJobResponse.builder()
                .jobId(j.getJobId())
                .jobType(j.getJobType())
                .status(j.getStatus())
                .idempotencyKey(j.getIdempotencyKey())
                .inputRefType(j.getInputRefType())
                .inputRefId(j.getInputRefId())
                .outputRefType(j.getOutputRefType())
                .outputRefId(j.getOutputRefId())
                .model(j.getModel())
                .promptVersion(j.getPromptVersion())
                .retryCount(j.getRetryCount())
                .maxRetries(j.getMaxRetries())
                .tokenInput(j.getTokenInput())
                .tokenOutput(j.getTokenOutput())
                .cost(j.getCost())
                .errorMessage(j.getErrorMessage())
                .startedAt(j.getStartedAt())
                .completedAt(j.getCompletedAt())
                .build();
    }

    private RubricResponse toRubricResponse(Rubric r) {
        return RubricResponse.builder()
                .rubricId(r.getRubricId())
                .certificateId(r.getCertificateId())
                .skill(r.getSkill())
                .name(r.getName())
                .description(r.getDescription())
                .criteriaJson(r.getCriteriaJson())
                .version(r.getVersion())
                .isActive(r.isActive())
                .build();
    }
}
