package adaptivelearning.module.certificates.entity;

import adaptivelearning.config.app.BaseEntity;
import adaptivelearning.shared.enums.ContentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "certificates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificate_id")
    private Long certificateId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @NotBlank
    @Size(max = 20)
    @Column(name = "code", unique = true, nullable = false, length = 20)
    private String code;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "total_score")
    private double totalScore;

    @Column(name = "passing_score")
    private double passingScore;

    @Column(name = "passing_band")
    private double passingBand;

    @Column(name = "skills", columnDefinition = "TEXT")
    private String skills;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private ContentStatus status = ContentStatus.DRAFT;
}
