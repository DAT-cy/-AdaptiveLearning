package adaptivelearning.module.speaking.dto.response;

import adaptivelearning.shared.enums.SpeakingStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpeakingSubmissionResponse {

    private Long submissionId;
    private Long attemptId;
    private Long userId;
    private Long questionId;
    private String audioUrl;
    private String transcriptText;
    private int durationSeconds;
    private SpeakingStatus status;
}
