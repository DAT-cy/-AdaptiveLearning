package adaptivelearning.module.attempts.dto.request;

import adaptivelearning.shared.enums.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for recording a violation event during an attempt")
public class ViolationRequest {

    @Schema(description = "Type of violation event", example = "TAB_SWITCH")
    private EventType eventType;

    @Schema(description = "Additional violation details as JSON")
    private String payloadJson;
}
