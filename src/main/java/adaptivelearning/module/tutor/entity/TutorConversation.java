package adaptivelearning.module.tutor.entity;

import adaptivelearning.config.app.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tutor_conversations")
@Getter @Setter
public class TutorConversation extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private Long userId;
    private Long certificateId;
    @Column(length = 200) private String title;
    @Column(columnDefinition = "TEXT") private String contextJson;
    @Column(nullable = false) private boolean isActive = true;
}
