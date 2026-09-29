package adaptivelearning.module.speaking.entity;

import adaptivelearning.config.app.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "speaking_evaluations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SpeakingEvaluation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evaluationId;

    @Column(name = "submission_id", nullable = false)
    private Long submissionId;

    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "overall_score")
    private Double overallScore;

    @Column(name = "criteria_json", columnDefinition = "JSON")
    private String criteriaJson;

    @Column(name = "errors_json", columnDefinition = "JSON")
    private String errorsJson;

    @Column(name = "transcript_json", columnDefinition = "JSON")
    private String transcriptJson;

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
}
