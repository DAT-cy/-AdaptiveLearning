package adaptivelearning.module.planner.dto.response;
import adaptivelearning.shared.enums.PlanStatus; import lombok.Builder; import lombok.Data; import java.time.LocalDate; import java.time.LocalDateTime;
@Data @Builder public class StudyPlanResponse { private Long planId; private String title; private Long certificateId; private Double targetScore; private LocalDate examDate; private int hoursPerWeek; private PlanStatus status; private int version; private long taskCount; private long completedCount; private LocalDateTime createdAt; }
