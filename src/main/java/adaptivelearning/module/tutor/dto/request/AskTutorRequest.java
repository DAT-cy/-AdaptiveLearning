package adaptivelearning.module.tutor.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "AskTutorRequest")
public class AskTutorRequest {
    @NotBlank
    private String content;
    private String contextJson;
}
