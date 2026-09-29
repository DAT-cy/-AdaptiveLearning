package adaptivelearning.module.ai.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Rubric response")
public class RubricResponse {

    @Schema(description = "Rubric ID", example = "1")
    private Long rubricId;

    @Schema(description = "Certificate ID", example = "1")
    private Long certificateId;

    @Schema(description = "Skill", example = "WRITING")
    private String skill;

    @Schema(description = "Rubric name", example = "IELTS Writing Task 2")
    private String name;

    @Schema(description = "Rubric description")
    private String description;

    @Schema(description = "Criteria JSON")
    private String criteriaJson;

    @Schema(description = "Version", example = "1")
    private int version;

    @Schema(description = "Whether rubric is active", example = "true")
    private boolean isActive;
}
