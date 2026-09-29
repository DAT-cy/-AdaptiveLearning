package adaptivelearning.module.ai.dto.response;

import adaptivelearning.shared.enums.AiJobStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AI evaluation response")
public class AiEvaluationResponse {

    @Schema(description = "Evaluation ID", example = "1")
    private Long evaluationId;

    @Schema(description = "Attempt ID", example = "1")
    private Long attemptId;

    @Schema(description = "Question ID", example = "10")
    private Long questionId;

    @Schema(description = "Rubric ID")
    private Long rubricId;

    @Schema(description = "User answer text")
    private String userAnswerText;

    @Schema(description = "Overall score", example = "7.5")
    private Double overallScore;

    @Schema(description = "Criteria scores JSON")
    private String criteriaJson;

    @Schema(description = "Errors JSON")
    private String errorsJson;

    @Schema(description = "Strengths JSON")
    private String strengthsJson;

    @Schema(description = "Rewrite suggestion")
    private String rewriteSuggestion;

    @Schema(description = "AI confidence 0.0-1.0", example = "0.92")
    private Double confidence;

    @Schema(description = "Model used", example = "gpt-4o-mini")
    private String model;

    @Schema(description = "Prompt version", example = "v1.0")
    private String promptVersion;

    @Schema(description = "Raw response reference ID")
    private String rawResponseId;

    @Schema(description = "Evaluation status", example = "SUCCEEDED")
    private AiJobStatus status;
}
