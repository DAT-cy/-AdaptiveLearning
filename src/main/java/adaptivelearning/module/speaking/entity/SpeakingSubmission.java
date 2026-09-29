package adaptivelearning.module.speaking.entity;

import adaptivelearning.config.app.BaseEntity;
import adaptivelearning.shared.enums.SpeakingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "speaking_submissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SpeakingSubmission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submissionId;

    @Column(name = "attempt_id")
    private Long attemptId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "audio_url")
    private String audioUrl;

    @Column(name = "transcript_text", columnDefinition = "TEXT")
    private String transcriptText;

    @Column(name = "duration_seconds")
    private int durationSeconds;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status", nullable = false)
    private SpeakingStatus status = SpeakingStatus.UPLOADED;
}
