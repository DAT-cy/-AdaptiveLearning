package adaptivelearning.module.ai.entity;

import adaptivelearning.config.app.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rubrics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Rubric extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rubricId;

    @NotNull
    @Column(name = "certificate_id", nullable = false)
    private Long certificateId;

    @NotBlank
    @Column(name = "skill", length = 50, nullable = false)
    private String skill;

    @NotBlank
    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(name = "criteria_json", columnDefinition = "JSON", nullable = false)
    private String criteriaJson;

    @Builder.Default
    @Column(name = "version", nullable = false)
    private int version = 1;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}
