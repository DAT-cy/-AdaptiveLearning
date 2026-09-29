package adaptivelearning.module.practice.entity;

import adaptivelearning.config.app.BaseEntity;
import adaptivelearning.shared.enums.QuestionDifficulty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity @Table(name = "practice_packages") @Data @Builder @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper = true)
public class PracticePackage extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long packageId;
    @NotBlank @Column(nullable = false, length = 200) private String title;
    @Column(columnDefinition = "TEXT") private String description;
    @NotNull @Column(nullable = false) private Long certificateId;
    private Long targetTopicId;
    @Enumerated(EnumType.STRING) private QuestionDifficulty targetDifficulty;
    @NotNull @Column(columnDefinition = "JSON", nullable = false) private String questionIdsJson;
    @Builder.Default private int estimatedMinutes = 0;
    @Builder.Default private int questionCount = 0;
    @Builder.Default private boolean isActive = true;
}
