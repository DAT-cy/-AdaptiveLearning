package adaptivelearning.module.adaptive.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.Data;

@Data
public class UpdateLearnerRequest {

    @NotNull
    private Long certificateId;

    private Map<String, Double> scoreBySkillJson;
}
