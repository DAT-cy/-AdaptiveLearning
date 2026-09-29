package adaptivelearning.module.attempts.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Single answer within an attempt")
public class AttemptAnswerResponse {

    @Schema(description = "Answer record ID", example = "1")
    private Long id;

    @Schema(description = "Question ID", example = "10")
    private Long questionId;

    @Schema(description = "User's answer as JSON", example = "{\"selected\":\"A\"}")
    private String userAnswerJson;

    @Schema(description = "Whether the answer is correct; null for essay/pending grading", nullable = true)
    private Boolean isCorrect;

    @Schema(description = "Points earned for this answer", example = "1.0")
    private Double pointsEarned;

    @Schema(description = "Time spent in seconds", example = "30")
    private int timeSpentSeconds;

    @Schema(description = "Order in which the question was answered", example = "1")
    private int orderAnswered;
}
