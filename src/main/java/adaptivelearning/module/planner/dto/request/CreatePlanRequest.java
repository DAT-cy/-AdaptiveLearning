package adaptivelearning.module.planner.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data; import java.time.LocalDate;
@Data public class CreatePlanRequest { private Long certificateId; private Double targetScore; @NotNull private LocalDate examDate; @NotNull private Integer hoursPerWeek; @NotBlank @Size(max=200) private String title; }
