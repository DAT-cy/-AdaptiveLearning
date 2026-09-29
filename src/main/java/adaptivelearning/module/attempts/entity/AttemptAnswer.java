package adaptivelearning.module.attempts.entity;

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
@Table(name = "attempt_answers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AttemptAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "attempt_id", nullable = false)
    private Long attemptId;

    @NotNull
    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "user_answer_json", columnDefinition = "JSON")
    private String userAnswerJson;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Builder.Default
    @Column(name = "points_earned", nullable = false)
    private Double pointsEarned = 0d;

    @Builder.Default
    @Column(name = "time_spent_seconds", nullable = false)
    private int timeSpentSeconds = 0;

    @Builder.Default
    @Column(name = "order_answered", nullable = false)
    private int orderAnswered = 0;
}
