package adaptivelearning.module.adaptive.entity;

import adaptivelearning.config.app.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "learner_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LearnerProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "certificate_id", nullable = false)
    private Long certificateId;

    @Column(name = "ability_by_skill_json", columnDefinition = "JSON")
    private String abilityBySkillJson;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "total_attempts")
    private int totalAttempts;
}
