package adaptivelearning.module.exams.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for adding or reordering a question in an exam")
public class ExamQuestionRequest {

    @Schema(description = "Question ID from the question bank", example = "10")
    private Long questionId;

    @Schema(description = "Display order of the question in the exam", example = "1")
    private int questionOrder;

    @Schema(description = "Override the question's default points; null uses default")
    private Double pointsOverride;

    @Schema(description = "Section name, e.g. 'Reading Part 1', 'Writing Task 2'", example = "Reading Part 1")
    private String sectionName;
}
