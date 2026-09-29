package adaptivelearning.module.planner.dto.response;
import adaptivelearning.shared.enums.*; import lombok.Builder; import lombok.Data; import java.time.LocalDate;
@Data @Builder public class PlanTaskResponse { private Long taskId; private Long planId; private LocalDate scheduledDate; private String skill; private PlanTaskType taskType; private int durationMinutes; private String description; private PlanTaskStatus status; }
