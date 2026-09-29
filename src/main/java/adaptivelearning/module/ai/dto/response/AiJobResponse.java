package adaptivelearning.module.ai.dto.response;

import adaptivelearning.shared.enums.AiJobStatus;
import adaptivelearning.shared.enums.AiJobType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AI job response")
public class AiJobResponse {

    @Schema(description = "Job ID", example = "1")
    private Long jobId;

    @Schema(description = "Job type", example = "GRADE_WRITING")
    private AiJobType jobType;

    @Schema(description = "Job status", example = "PENDING")
    private AiJobStatus status;

    @Schema(description = "Idempotency key", example = "grade-1-10-abc123")
    private String idempotencyKey;

    @Schema(description = "Input reference type", example = "ATTEMPT_ANSWER")
    private String inputRefType;

    @Schema(description = "Input reference ID", example = "1")
    private Long inputRefId;

    @Schema(description = "Output reference type", example = "AI_EVALUATION")
    private String outputRefType;

    @Schema(description = "Output reference ID", example = "1")
    private Long outputRefId;

    @Schema(description = "Model name", example = "gpt-4o-mini")
    private String model;

    @Schema(description = "Prompt version", example = "v1.0")
    private String promptVersion;

    @Schema(description = "Retry count", example = "0")
    private int retryCount;

    @Schema(description = "Max retries", example = "3")
    private int maxRetries;

    @Schema(description = "Input tokens used", example = "500")
    private int tokenInput;

    @Schema(description = "Output tokens used", example = "800")
    private int tokenOutput;

    @Schema(description = "Cost in USD", example = "0.02")
    private double cost;

    @Schema(description = "Error message if failed")
    private String errorMessage;

    @Schema(description = "Started at")
    private LocalDateTime startedAt;

    @Schema(description = "Completed at")
    private LocalDateTime completedAt;
}
