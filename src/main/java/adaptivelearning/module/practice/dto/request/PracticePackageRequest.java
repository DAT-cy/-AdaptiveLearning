package adaptivelearning.module.practice.dto.request;

import adaptivelearning.shared.enums.QuestionDifficulty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Schema(description = "Practice package request")
public class PracticePackageRequest {
    @NotNull @NotBlank @Schema(description = "Package title") private String title;
    @Schema(description = "Description") private String description;
    @NotNull @Schema(description = "Certificate ID") private Long certificateId;
    @Schema(description = "Target topic ID") private Long targetTopicId;
    @Schema(description = "Target difficulty") private QuestionDifficulty targetDifficulty;
    @NotNull @Schema(description = "Question IDs JSON array") private String questionIdsJson;
    @Schema(description = "Estimated minutes") private Integer estimatedMinutes;
}
