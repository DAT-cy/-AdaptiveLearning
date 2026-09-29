package adaptivelearning.module.ai.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create or update a rubric")
public class RubricRequest {

    @NotNull(message = "certificateId is required")
    @Schema(description = "Certificate ID", example = "1")
    private Long certificateId;

    @NotBlank(message = "skill is required")
    @Schema(description = "Skill type, e.g. WRITING, SPEAKING", example = "WRITING")
    private String skill;

    @NotBlank(message = "name is required")
    @Schema(description = "Rubric name", example = "IELTS Writing Task 2")
    private String name;

    @Schema(description = "Rubric description")
    private String description;

    @NotNull(message = "criteriaJson is required")
    @Schema(description = "Criteria JSON array", example = "[{\"name\":\"Coherence\",\"maxScore\":9,\"description\":\"...\"}]")
    private String criteriaJson;
}
