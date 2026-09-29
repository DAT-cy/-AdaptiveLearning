package adaptivelearning.module.adaptive.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LearnerProfileResponse {

    private Long profileId;
    private Long userId;
    private Long certificateId;
    private String abilityBySkillJson;
    private LocalDateTime lastUpdated;
    private int totalAttempts;
}
