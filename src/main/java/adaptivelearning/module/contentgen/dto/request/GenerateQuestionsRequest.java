package adaptivelearning.module.contentgen.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(name = "GenerateQuestionsRequest")
public class GenerateQuestionsRequest {
    @NotNull
    private Long certificateId;
    private Long topicId;
    @NotBlank
    private String questionType;
    private String difficulty;
    @Min(1) @Max(20)
    private int count = 5;
    private String instructions;
}
