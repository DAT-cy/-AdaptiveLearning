package adaptivelearning.module.questions.entity;

import adaptivelearning.config.app.BaseEntity;
import adaptivelearning.shared.enums.ContentStatus;
import adaptivelearning.shared.enums.QuestionDifficulty;
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

/**
 * Question bank entity representing a reusable question that can be included in exams.
 */
@Entity
@Table(name = "questions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @NotNull
    @Column(name = "certificate_id", nullable = false)
    private Long certificateId;

    /** References the topic/type from the topics table. */
    @NotNull
    @Column(name = "topic_id", nullable = false)
    private Long topicId;

    /** e.g. MULTIPLE_CHOICE, TRUE_FALSE, ESSAY, FILL_BLANK */
    @NotBlank
    @Column(name = "question_type", length = 50, nullable = false)
    private String questionType;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "difficulty", nullable = false)
    private QuestionDifficulty difficulty = QuestionDifficulty.MEDIUM;

    /** Question body; supports HTML/markdown. */
    @NotBlank
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    /** Structured content (images, audio refs). */
    @Column(name = "content_json", columnDefinition = "JSON")
    private String contentJson;

    /** Choices for MCQ: [{"label":"A","text":"..."}] */
    @Column(name = "options_json", columnDefinition = "JSON")
    private String optionsJson;

    /** Correct answer(s) as JSON. */
    @Column(name = "correct_answer", columnDefinition = "TEXT")
    private String correctAnswer;

    /** Explanation for the correct answer. */
    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;

    @Builder.Default
    @Column(name = "points", nullable = false)
    private double points = 1.0;

    /** Estimated time per question, in seconds. */
    @Builder.Default
    @Column(name = "estimated_time_seconds", nullable = false)
    private int estimatedTimeSeconds = 0;

    /** Comma-separated tags. */
    @Column(name = "tags", length = 500)
    private String tags;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status", nullable = false)
    private ContentStatus status = ContentStatus.DRAFT;

    @Builder.Default
    @Column(name = "version", nullable = false)
    private int version = 1;

    /** Creator username, stored alongside BaseEntity audit fields. */
    @Column(name = "created_by", length = 100)
    private String createdBy;
}
