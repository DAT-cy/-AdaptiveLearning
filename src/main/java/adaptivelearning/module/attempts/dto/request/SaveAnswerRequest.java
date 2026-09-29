package adaptivelearning.module.attempts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for saving/updating an answer (autosave)")
public class SaveAnswerRequest {

    @NotNull(message = "questionId is required")
    @Schema(description = "Question ID being answered", example = "10")
    private Long questionId;

    @NotNull(message = "userAnswerJson is required")
    @Schema(description = "User's answer as JSON", example = "{\"selected\":\"A\"}")
    private String userAnswerJson;

    @Schema(description = "Time spent on this question in seconds", example = "30")
    private int timeSpentSeconds;
}
