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
@Table(name = "gap_analyses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GapAnalysis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gapAnalysisId;

    @NotNull
    @Column(name = "attempt_id", nullable = false)
    private Long attemptId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "certificate_id", nullable = false)
    private Long certificateId;

    @Column(name = "accuracy_by_skill_json", columnDefinition = "JSON")
    private String accuracyBySkillJson;

    @Column(name = "accuracy_by_topic_json", columnDefinition = "JSON")
    private String accuracyByTopicJson;

    @Column(name = "accuracy_by_difficulty_json", columnDefinition = "JSON")
    private String accuracyByDifficultyJson;

    @Column(name = "weak_topics_json", columnDefinition = "JSON")
    private String weakTopicsJson;

    @Column(name = "strong_topics_json", columnDefinition = "JSON")
    private String strongTopicsJson;

    private Double overallAccuracy;

    private LocalDateTime generatedAt;
}
