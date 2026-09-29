package adaptivelearning.module.speaking.service;

import adaptivelearning.module.speaking.dto.request.SpeakingEvaluationRequest;
import adaptivelearning.module.speaking.dto.request.SpeakingSubmissionRequest;
import adaptivelearning.module.speaking.dto.response.SpeakingEvaluationResponse;
import adaptivelearning.module.speaking.dto.response.SpeakingSubmissionResponse;
import java.util.List;

public interface SpeakingService {

    SpeakingSubmissionResponse submit(Long userId, SpeakingSubmissionRequest request);

    SpeakingSubmissionResponse getSubmission(Long userId, Long id);

    List<SpeakingSubmissionResponse> listByAttempt(Long userId, Long attemptId);

    SpeakingEvaluationResponse evaluate(Long userId, Long submissionId, Long rubricId);

    List<SpeakingEvaluationResponse> listEvaluations(Long submissionId);
}
