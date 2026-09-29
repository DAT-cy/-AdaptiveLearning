package adaptivelearning.module.attempts.dto.response;

import adaptivelearning.shared.enums.AttemptStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Exam attempt summary response")
public class AttemptResponse {

    @Schema(description = "Attempt ID", example = "1")
    private Long attemptId;

    @Schema(description = "User ID", example = "5")
    private Long userId;

    @Schema(description = "Exam ID", example = "1")
    private Long examId;

    @Schema(description = "Current attempt status")
    private AttemptStatus status;

    @Schema(description = "When the attempt started")
    private LocalDateTime startedAt;

    @Schema(description = "When the attempt expires")
    private LocalDateTime expiresAt;

    @Schema(description = "When the attempt was submitted", nullable = true)
    private LocalDateTime submittedAt;

    @Schema(description = "Raw score achieved", nullable = true)
    private Double score;

    @Schema(description = "Maximum possible score", nullable = true)
    private Double maxScore;

    @Schema(description = "Band score, e.g. IELTS 7.0", nullable = true)
    private Double bandScore;

    @Schema(description = "Score breakdown by skill/topic as JSON")
    private String scoreBreakdownJson;

    @Schema(description = "Number of violation events", example = "0")
    private int violationCount;

    @Schema(description = "Client IP address")
    private String ipAddress;

    @Schema(description = "Client user agent string")
    private String userAgent;
}
