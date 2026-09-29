package adaptivelearning.module.tutor.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "CreateConversationRequest")
public class CreateConversationRequest {
    private Long certificateId;
    @Schema(maxLength = 200)
    private String title;
}
