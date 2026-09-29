package adaptivelearning.module.analytics.entity;

import adaptivelearning.config.app.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "learning_progress")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LearningProgress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long progressId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "certificate_id", nullable = false)
    private Long certificateId;

    @Column(name = "period", length = 20)
    private String period;

    @Column(name = "mastery_by_skill_json", columnDefinition = "JSON")
    private String masteryBySkillJson;

    @Column(name = "score_trend_json", columnDefinition = "JSON")
    private String scoreTrendJson;

    private Double averageScore;

    @Builder.Default
    @Column(name = "total_attempts", nullable = false)
    private int totalAttempts = 0;

    @Column(name = "snapshot_at")
    private LocalDateTime snapshotAt;
}
