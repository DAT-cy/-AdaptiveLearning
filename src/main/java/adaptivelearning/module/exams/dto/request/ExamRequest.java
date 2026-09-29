package adaptivelearning.module.exams.dto.request;

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
@Schema(description = "Request body for creating or updating an exam")
public class ExamRequest {

    @NotNull(message = "certificateId is required")
    @Schema(description = "Certificate ID this exam belongs to", example = "1")
    private Long certificateId;

    @NotBlank(message = "title is required")
    @Schema(description = "Exam title", example = "IELTS Reading Practice Test 1")
    private String title;

    @Schema(description = "Exam description", example = "A full-length IELTS reading practice test.")
    private String description;

    @NotNull(message = "durationMinutes is required")
    @Schema(description = "Duration of the exam in minutes", example = "60")
    private int durationMinutes;

    @Schema(description = "Minimum score to pass", example = "40.0")
    private double passingScore;

    @Schema(description = "Section/skill distribution rules as JSON")
    private String blueprintJson;

    @Schema(description = "Band/score conversion rules as JSON")
    private String scoringJson;
}
