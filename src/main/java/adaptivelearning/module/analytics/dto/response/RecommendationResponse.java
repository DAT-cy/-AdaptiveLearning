package adaptivelearning.module.analytics.dto.response;

import adaptivelearning.shared.enums.RecommendationPriority;
import adaptivelearning.shared.enums.RecommendationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Schema(description = "Recommendation response")
public class RecommendationResponse {
    @Schema(description = "Recommendation ID") private Long recommendationId;
    @Schema(description = "User ID") private Long userId;
    @Schema(description = "Gap analysis ID") private Long gapAnalysisId;
    @Schema(description = "Practice package ID") private Long practicePackageId;
    @Schema(description = "Recommendation reason") private String reason;
    @Schema(description = "Priority") private RecommendationPriority priority;
    @Schema(description = "Status") private RecommendationStatus status;
}
