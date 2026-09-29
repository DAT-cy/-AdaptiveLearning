package adaptivelearning.module.questions.dto.response;

import adaptivelearning.shared.enums.ContentStatus;
import adaptivelearning.shared.enums.QuestionDifficulty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response payload exposing all question fields.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Question response payload")
public class QuestionResponse {

    @Schema(description = "Question ID", example = "1")
    private Long questionId;

    @Schema(description = "Certificate ID the question belongs to", example = "1")
    private Long certificateId;

    @Schema(description = "Topic/type ID from the topics table", example = "10")
    private Long topicId;

    @Schema(description = "Question type", example = "MULTIPLE_CHOICE")
    private String questionType;

    @Schema(description = "Question difficulty", example = "MEDIUM")
    private QuestionDifficulty difficulty;

    @Schema(description = "Question body; supports HTML/markdown")
    private String content;

    @Schema(description = "Structured content (images, audio refs) as JSON")
    private String contentJson;

    @Schema(description = "Choices for MCQ as JSON")
    private String optionsJson;

    @Schema(description = "Correct answer(s) as JSON")
    private String correctAnswer;

    @Schema(description = "Explanation for the correct answer")
    private String explanation;

    @Schema(description = "Points awarded for this question", example = "1.0")
    private double points;

    @Schema(description = "Estimated time per question in seconds", example = "60")
    private int estimatedTimeSeconds;

    @Schema(description = "Comma-separated tags", example = "java,spring,core")
    private String tags;

    @Schema(description = "Question status", example = "APPROVED")
    private ContentStatus status;

    @Schema(description = "Question version", example = "1")
    private int version;

    @Schema(description = "Username of the creator")
    private String createdBy;

    @Schema(description = "Creation timestamp")
    private Date createdAt;

    @Schema(description = "Username of the last updater")
    private String updatedId;

    @Schema(description = "Last update timestamp")
    private Date updatedAt;
}
