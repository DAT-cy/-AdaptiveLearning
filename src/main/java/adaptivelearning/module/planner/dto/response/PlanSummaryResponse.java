package adaptivelearning.module.planner.dto.response;
import lombok.Builder; import lombok.Data;
@Data @Builder public class PlanSummaryResponse { private Long planId; private String title; private double progressPercent; private long overdueCount; private int upcomingTasks; private String skillDistributionJson; }
