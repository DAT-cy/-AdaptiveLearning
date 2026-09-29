package adaptivelearning.module.ai.entity;

import adaptivelearning.config.app.BaseEntity;
import adaptivelearning.shared.enums.AiJobStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "ai_evaluations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AiEvaluation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evaluationId;

    @NotNull
    @Column(name = "attempt_id", nullable = false)
    private Long attemptId;

    @NotNull
    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "rubric_id")
    private Long rubricId;

    @Column(name = "user_answer_text", columnDefinition = "TEXT")
    private String userAnswerText;

    @Column(name = "overall_score")
    private Double overallScore;

    @Column(name = "criteria_json", columnDefinition = "JSON")
    private String criteriaJson;

    @Column(name = "errors_json", columnDefinition = "JSON")
    private String errorsJson;

    @Column(name = "strengths_json", columnDefinition = "JSON")
    private String strengthsJson;

    @Column(name = "rewrite_suggestion", columnDefinition = "TEXT")
    private String rewriteSuggestion;

    @Column(name = "confidence")
    private Double confidence;

    @Column(name = "model", length = 100)
    private String model;

    @Column(name = "prompt_version", length = 50)
    private String promptVersion;

    @Column(name = "raw_response_id", length = 100)
    private String rawResponseId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status", nullable = false)
    private AiJobStatus status = AiJobStatus.PENDING;
}
