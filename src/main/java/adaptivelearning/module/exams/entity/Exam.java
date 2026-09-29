package adaptivelearning.module.exams.entity;

import adaptivelearning.config.app.BaseEntity;
import adaptivelearning.shared.enums.ContentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "exams")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Exam extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examId;

    @NotNull
    @Column(name = "certificate_id", nullable = false)
    private Long certificateId;

    @NotBlank
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Builder.Default
    @Column(name = "total_questions", nullable = false)
    private int totalQuestions = 0;

    @Builder.Default
    @Column(name = "total_points", nullable = false)
    private double totalPoints = 0;

    @Builder.Default
    @Column(name = "passing_score", nullable = false)
    private double passingScore = 0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status", nullable = false)
    private ContentStatus status = ContentStatus.DRAFT;

    @Column(name = "blueprint_json", columnDefinition = "JSON")
    private String blueprintJson;

    @Column(name = "scoring_json", columnDefinition = "JSON")
    private String scoringJson;

    @Builder.Default
    @Column(name = "version", nullable = false)
    private int version = 1;
}
