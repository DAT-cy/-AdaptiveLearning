package adaptivelearning.module.ai.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to grade a writing/speaking answer via AI")
public class AiGradeRequest {

    @NotNull(message = "attemptId is required")
    @Schema(description = "Exam attempt ID", example = "1")
    private Long attemptId;

    @NotNull(message = "questionId is required")
    @Schema(description = "Question ID", example = "10")
    private Long questionId;

    @NotNull(message = "userAnswerText is required")
    @Schema(description = "User answer text (essay or transcript)", example = "This is my essay...")
    private String userAnswerText;

    @Schema(description = "Optional rubric ID to use for grading", example = "1")
    private Long rubricId;
}
