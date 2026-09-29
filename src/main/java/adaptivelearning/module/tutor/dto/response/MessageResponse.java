package adaptivelearning.module.tutor.dto.response;
import adaptivelearning.shared.enums.TutorRole; import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder; import lombok.Value;

@Value @Builder @Schema(name = "MessageResponse")
public class MessageResponse { Long id; Long conversationId; TutorRole role; String content; String citationsJson; String model; int tokenCount; LocalDateTime createdAt; }
