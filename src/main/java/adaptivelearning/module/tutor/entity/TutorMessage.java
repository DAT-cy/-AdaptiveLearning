package adaptivelearning.module.tutor.entity;

import adaptivelearning.shared.enums.TutorRole;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name = "tutor_messages") @Getter @Setter
public class TutorMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private Long conversationId;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private TutorRole role;
    @Column(nullable = false, columnDefinition = "TEXT") private String content;
    @Column(columnDefinition = "TEXT") private String citationsJson;
    private String model;
    private int tokenCount;
    @Column(nullable = false) private LocalDateTime createdAt = LocalDateTime.now();
}
