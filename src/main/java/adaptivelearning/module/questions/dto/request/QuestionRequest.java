package adaptivelearning.module.questions.dto.request;

import adaptivelearning.shared.enums.QuestionDifficulty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for creating or updating a question.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for creating or updating a question")
public class QuestionRequest {

    @NotNull
    @Schema(description = "Certificate ID the question belongs to", example = "1")
    private Long certificateId;

    @NotNull
    @Schema(description = "Topic/type ID from the topics table", example = "10")
    private Long topicId;

    @NotNull
    @NotBlank
    @Schema(description = "Question type", example = "MULTIPLE_CHOICE")
    private String questionType;

    @Schema(description = "Question difficulty", example = "MEDIUM")
    private QuestionDifficulty difficulty;

    @NotNull
    @NotBlank
    @Schema(description = "Question body; supports HTML/markdown")
    private String content;

    @Schema(description = "Structured content (images, audio refs) as JSON")
    private String contentJson;

    @Schema(description = "Choices for MCQ as JSON, e.g. [{\"label\":\"A\",\"text\":\"...\"}]")
    private String optionsJson;

    @Schema(description = "Correct answer(s) as JSON")
    private String correctAnswer;

    @Schema(description = "Explanation for the correct answer")
    private String explanation;

    @Schema(description = "Points awarded for this question", example = "1.0")
    private Double points;

    @Schema(description = "Estimated time per question in seconds", example = "60")
    private Integer estimatedTimeSeconds;

    @Schema(description = "Comma-separated tags", example = "java,spring,core")
    private String tags;
}
