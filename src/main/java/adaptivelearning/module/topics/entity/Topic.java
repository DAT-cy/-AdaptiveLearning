package adaptivelearning.module.topics.entity;

import adaptivelearning.config.app.BaseEntity;
import adaptivelearning.shared.enums.TopicType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "topics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Long topicId;

    @NotBlank
    @Size(max = 150)
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Size(max = 50)
    @Column(name = "code", length = 50)
    private String code;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TopicType type;

    @Column(name = "certificate_id")
    private Long certificateId;

    @Column(name = "parent_id")
    private Long parentId;

    @Builder.Default
    @Column(name = "sort_order")
    private int sortOrder = 0;

    @Builder.Default
    @Column(name = "is_active")
    private boolean isActive = true;
}
