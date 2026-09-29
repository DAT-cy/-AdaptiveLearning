package adaptivelearning.module.ai.entity;

import adaptivelearning.config.app.BaseEntity;
import adaptivelearning.shared.enums.AiJobStatus;
import adaptivelearning.shared.enums.AiJobType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ai_jobs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AiJob extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", nullable = false)
    private AiJobType jobType;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status", nullable = false)
    private AiJobStatus status = AiJobStatus.PENDING;

    @Column(name = "idempotency_key", unique = true, length = 100)
    private String idempotencyKey;

    @Column(name = "input_ref_type", length = 50)
    private String inputRefType;

    @Column(name = "input_ref_id")
    private Long inputRefId;

    @Column(name = "output_ref_type", length = 50)
    private String outputRefType;

    @Column(name = "output_ref_id")
    private Long outputRefId;

    @Column(name = "model", length = 100)
    private String model;

    @Column(name = "prompt_version", length = 50)
    private String promptVersion;

    @Builder.Default
    @Column(name = "retry_count", nullable = false)
    private int retryCount = 0;

    @Builder.Default
    @Column(name = "max_retries", nullable = false)
    private int maxRetries = 3;

    @Builder.Default
    @Column(name = "token_input", nullable = false)
    private int tokenInput = 0;

    @Builder.Default
    @Column(name = "token_output", nullable = false)
    private int tokenOutput = 0;

    @Builder.Default
    @Column(name = "cost", nullable = false)
    private double cost = 0;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
