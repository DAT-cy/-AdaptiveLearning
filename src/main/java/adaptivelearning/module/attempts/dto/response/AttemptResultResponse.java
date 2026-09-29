package adaptivelearning.module.attempts.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Exam attempt result after grading")
public class AttemptResultResponse {

    @Schema(description = "Attempt ID", example = "1")
    private Long attemptId;

    @Schema(description = "Raw score achieved", example = "32.0")
    private Double score;

    @Schema(description = "Maximum possible score", example = "40.0")
    private Double maxScore;

    @Schema(description = "Band score, e.g. IELTS 7.0", example = "7.0", nullable = true)
    private Double bandScore;

    @Schema(description = "Total number of questions", example = "40")
    private int totalQuestions;

    @Schema(description = "Number of correct answers", example = "28")
    private int correctCount;

    @Schema(description = "Number of wrong answers", example = "8")
    private int wrongCount;

    @Schema(description = "Number of unanswered questions", example = "4")
    private int unansweredCount;

    @Schema(description = "Score breakdown by skill/topic as JSON")
    private String scoreBreakdownJson;

    @Schema(description = "Total time taken in seconds", example = "3200")
    private long timeTakenSeconds;

    @Schema(description = "Number of violation events", example = "0")
    private int violationCount;
}
