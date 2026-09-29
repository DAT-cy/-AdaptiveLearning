package adaptivelearning.module.speaking.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpeakingEvaluationResponse {

    private Long evaluationId;
    private Long submissionId;
    private Long questionId;
    private Double overallScore;
    private String criteriaJson;
    private String errorsJson;
    private String transcriptJson;
    private String strengthsJson;
    private String rewriteSuggestion;
    private Double confidence;
    private String model;
    private String promptVersion;
}
