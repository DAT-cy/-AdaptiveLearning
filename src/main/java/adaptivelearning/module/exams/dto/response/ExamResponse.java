package adaptivelearning.module.exams.dto.response;

import adaptivelearning.shared.enums.ContentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Exam summary response")
public class ExamResponse {

    @Schema(description = "Exam ID", example = "1")
    private Long examId;

    @Schema(description = "Certificate ID", example = "1")
    private Long certificateId;

    @Schema(description = "Exam title", example = "IELTS Reading Practice Test 1")
    private String title;

    @Schema(description = "Exam description")
    private String description;

    @Schema(description = "Duration in minutes", example = "60")
    private int durationMinutes;

    @Schema(description = "Total number of questions", example = "40")
    private int totalQuestions;

    @Schema(description = "Total points available", example = "40.0")
    private double totalPoints;

    @Schema(description = "Passing score", example = "28.0")
    private double passingScore;

    @Schema(description = "Exam status")
    private ContentStatus status;

    @Schema(description = "Blueprint rules as JSON")
    private String blueprintJson;

    @Schema(description = "Scoring rules as JSON")
    private String scoringJson;

    @Schema(description = "Exam version", example = "1")
    private int version;

    @Schema(description = "Number of questions linked to this exam", example = "40")
    private int questionCount;

    @Schema(description = "Created timestamp")
    private Date createdAt;

    @Schema(description = "Updated timestamp")
    private Date updatedAt;
}
