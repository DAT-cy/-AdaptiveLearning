package adaptivelearning.module.tutor.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder; import lombok.Value;

@Value @Builder @Schema(name = "ConversationResponse")
public class ConversationResponse { Long id; Long userId; Long certificateId; String title; boolean isActive; LocalDateTime createdAt; long messageCount; }
