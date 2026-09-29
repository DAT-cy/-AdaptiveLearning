package adaptivelearning.module.planner.dto.request;
import adaptivelearning.shared.enums.PlanTaskStatus; import jakarta.validation.constraints.NotNull; import lombok.Data;
@Data public class UpdateTaskRequest { @NotNull private PlanTaskStatus status; private String notes; }
