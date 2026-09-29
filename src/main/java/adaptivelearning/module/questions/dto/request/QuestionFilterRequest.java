package adaptivelearning.module.questions.dto.request;

import adaptivelearning.shared.enums.ContentStatus;
import adaptivelearning.shared.enums.QuestionDifficulty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Optional filter criteria used when listing questions.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Optional filter criteria for listing questions")
public class QuestionFilterRequest {

    @Schema(description = "Filter by certificate ID", example = "1")
    private Long certificateId;

    @Schema(description = "Filter by topic/type ID", example = "10")
    private Long topicId;

    @Schema(description = "Filter by question type", example = "MULTIPLE_CHOICE")
    private String questionType;

    @Schema(description = "Filter by difficulty", example = "MEDIUM")
    private QuestionDifficulty difficulty;

    @Schema(description = "Filter by status", example = "APPROVED")
    private ContentStatus status;

    @Schema(description = "Filter by tag (comma-separated tags contain this value)", example = "spring")
    private String tag;
}
