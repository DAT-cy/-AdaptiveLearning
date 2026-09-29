package adaptivelearning.module.speaking.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SpeakingEvaluationRequest {

    @NotNull(message = "submissionId is required")
    private Long submissionId;

    private Long rubricId;
}
