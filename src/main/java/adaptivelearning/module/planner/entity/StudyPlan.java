package adaptivelearning.module.planner.entity;

import adaptivelearning.config.app.BaseEntity;
import adaptivelearning.shared.enums.PlanStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Table(name="study_plans") @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class StudyPlan extends BaseEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long planId;
    @Column(nullable=false) private Long userId;
    private Long certificateId; private Double targetScore; private LocalDate examDate; private int hoursPerWeek;
    @Enumerated(EnumType.STRING) @Builder.Default @Column(nullable=false) private PlanStatus status = PlanStatus.ACTIVE;
    private String title; @Builder.Default private int version = 1;
    @Column(columnDefinition="JSON") private String planJson;
    @Column(name="plan_created_at", nullable=false) @Builder.Default private LocalDateTime planCreatedAt = LocalDateTime.now();
}
