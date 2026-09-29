package adaptivelearning.module.practice.dto.response;

import adaptivelearning.shared.enums.QuestionDifficulty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Schema(description = "Practice package response")
public class PracticePackageResponse {
    @Schema(description = "Package ID") private Long packageId;
    private String title;
    private String description;
    private Long certificateId;
    private Long targetTopicId;
    private QuestionDifficulty targetDifficulty;
    private String questionIdsJson;
    private int estimatedMinutes;
    private int questionCount;
    private boolean isActive;
}
