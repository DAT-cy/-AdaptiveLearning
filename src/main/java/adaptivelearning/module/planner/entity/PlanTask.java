package adaptivelearning.module.planner.entity;

import adaptivelearning.shared.enums.PlanTaskStatus;
import adaptivelearning.shared.enums.PlanTaskType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity @Table(name="plan_tasks") @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class PlanTask {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long taskId;
    @Column(nullable=false) private Long planId;
    private LocalDate scheduledDate;
    private String skill;
    private Long topicId;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private PlanTaskType taskType;
    private int durationMinutes;
    @Column(columnDefinition="TEXT") private String description;
    @Enumerated(EnumType.STRING) @Builder.Default @Column(nullable=false) private PlanTaskStatus status = PlanTaskStatus.PENDING;
    @Column(nullable=false) @Builder.Default private LocalDateTime createdAt = LocalDateTime.now();
}
