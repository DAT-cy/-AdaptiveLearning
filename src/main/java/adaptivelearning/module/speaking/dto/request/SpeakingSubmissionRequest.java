package adaptivelearning.module.speaking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SpeakingSubmissionRequest {

    private Long attemptId;

    @NotNull(message = "questionId is required")
    private Long questionId;

    @NotBlank(message = "transcriptText is required for MVP")
    private String transcriptText;
}
