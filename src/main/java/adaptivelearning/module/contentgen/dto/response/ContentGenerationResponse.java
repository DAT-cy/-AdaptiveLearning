package adaptivelearning.module.contentgen.dto.response;
import adaptivelearning.shared.enums.GenerationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder; import lombok.Value;

@Value @Builder @Schema(name = "ContentGenerationResponse")
public class ContentGenerationResponse {
    Long generationId; Long requestedBy; Long certificateId; Long topicId;
    String generationType; String prompt; String parametersJson; String resultJson;
    GenerationStatus status; String model; int itemCount;
    String errorMessage; LocalDateTime startedAt; LocalDateTime completedAt;
}
