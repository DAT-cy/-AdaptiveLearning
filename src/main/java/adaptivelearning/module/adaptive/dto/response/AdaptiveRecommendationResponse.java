package adaptivelearning.module.adaptive.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdaptiveRecommendationResponse {

    private String skill;
    private String targetDifficulty;
    private Double confidence;
    private String reason;
}
