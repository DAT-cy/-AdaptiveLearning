package adaptivelearning.module.attempts.service;

import adaptivelearning.module.attempts.dto.request.SaveAnswerRequest;
import adaptivelearning.module.attempts.dto.request.SubmitAttemptRequest;
import adaptivelearning.module.attempts.dto.request.ViolationRequest;
import adaptivelearning.module.attempts.dto.response.AttemptDetailResponse;
import adaptivelearning.module.attempts.dto.response.AttemptResponse;
import adaptivelearning.module.attempts.dto.response.AttemptResultResponse;

import java.util.List;

public interface AttemptService {

    AttemptResponse startAttempt(Long userId, Long examId);

    AttemptDetailResponse getAttempt(Long attemptId);

    void saveAnswer(Long attemptId, Long userId, SaveAnswerRequest request);

    AttemptResultResponse submitAttempt(Long attemptId, Long userId, SubmitAttemptRequest request);

    AttemptResultResponse getResult(Long attemptId);

    List<AttemptResponse> getHistory(Long userId);

    void recordViolation(Long attemptId, ViolationRequest request);

    void expireOverdueAttempts();
}
