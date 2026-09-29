package adaptivelearning.module.ai.service;

import adaptivelearning.module.ai.dto.request.AiGradeRequest;
import adaptivelearning.module.ai.dto.request.RubricRequest;
import adaptivelearning.module.ai.dto.response.AiEvaluationResponse;
import adaptivelearning.module.ai.dto.response.AiJobResponse;
import adaptivelearning.module.ai.dto.response.RubricResponse;
import java.util.List;

public interface AiService {
    AiEvaluationResponse gradeWriting(AiGradeRequest request);
    AiEvaluationResponse getEvaluation(Long evaluationId);
    List<AiEvaluationResponse> getEvaluationByAttempt(Long attemptId);
    RubricResponse createRubric(RubricRequest request);
    RubricResponse updateRubric(Long rubricId, RubricRequest request);
    List<RubricResponse> getRubricsByCertificate(Long certificateId);
    AiJobResponse submitGradeJob(AiGradeRequest request);
    void processPendingJobs();
    AiJobResponse retryJob(Long jobId);
    List<AiJobResponse> getJobs();
}
