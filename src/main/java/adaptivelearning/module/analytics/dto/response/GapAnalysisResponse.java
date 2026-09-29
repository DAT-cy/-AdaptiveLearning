package adaptivelearning.module.analytics.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Schema(description = "Gap analysis response")
public class GapAnalysisResponse {
    @Schema(description = "Gap analysis ID") private Long gapAnalysisId;
    @Schema(description = "Attempt ID") private Long attemptId;
    @Schema(description = "User ID") private Long userId;
    @Schema(description = "Certificate ID") private Long certificateId;
    @Schema(description = "Accuracy by skill") private String accuracyBySkillJson;
    @Schema(description = "Accuracy by topic") private String accuracyByTopicJson;
    @Schema(description = "Accuracy by difficulty") private String accuracyByDifficultyJson;
    @Schema(description = "Weak topics") private String weakTopicsJson;
    @Schema(description = "Strong topics") private String strongTopicsJson;
    @Schema(description = "Overall accuracy") private Double overallAccuracy;
    @Schema(description = "Generated at") private LocalDateTime generatedAt;
}
