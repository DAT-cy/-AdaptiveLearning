package adaptivelearning.module.exams.entity;

import adaptivelearning.config.app.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exam_questions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExamQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "exam_id", nullable = false)
    private Long examId;

    @NotNull
    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Builder.Default
    @Column(name = "question_order", nullable = false)
    private int questionOrder = 0;

    @Column(name = "points_override")
    private Double pointsOverride;

    @Column(name = "section_name")
    private String sectionName;
}
