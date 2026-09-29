package adaptivelearning.module.attempts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for submitting an exam attempt with final answers")
public class SubmitAttemptRequest {

    @Schema(description = "Final list of answers at the time of submission")
    private List<SaveAnswerRequest> answers;
}
