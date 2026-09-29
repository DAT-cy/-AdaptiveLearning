package adaptivelearning.module.attempts.dto.request;

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
@Schema(description = "Request body to start an exam attempt")
public class StartAttemptRequest {

    @NotNull(message = "examId is required")
    @Schema(description = "Exam ID to start an attempt for", example = "1")
    private Long examId;
}
