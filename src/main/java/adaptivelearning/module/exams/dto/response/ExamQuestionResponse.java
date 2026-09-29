package adaptivelearning.module.exams.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Exam question response")
public class ExamQuestionResponse {

    @Schema(description = "Exam-question mapping ID", example = "1")
    private Long id;

    @Schema(description = "Exam ID", example = "1")
    private Long examId;

    @Schema(description = "Question ID from the question bank", example = "10")
    private Long questionId;

    @Schema(description = "Display order", example = "1")
    private int questionOrder;

    @Schema(description = "Points override; null means use default")
    private Double pointsOverride;

    @Schema(description = "Section name", example = "Reading Part 1")
    private String sectionName;
}
