package adaptivelearning.module.analytics.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Schema(description = "Learning progress overview")
public class ProgressResponse {
    @Schema(description = "User ID") private Long userId;
    @Schema(description = "Certificate ID") private Long certificateId;
    @Schema(description = "Average score") private Double averageScore;
    @Schema(description = "Total attempts") private int totalAttempts;
    @Schema(description = "Mastery by skill") private String masteryBySkillJson;
    @Schema(description = "Score trend") private String scoreTrendJson;
    @Schema(description = "Recent attempts") private List<AttemptSummary> recentAttempts;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @Schema(description = "Attempt summary")
    public static class AttemptSummary {
        @Schema(description = "Attempt ID") private Long attemptId;
        @Schema(description = "Exam title") private String examTitle;
        @Schema(description = "Score") private Double score;
        @Schema(description = "Band score") private Double bandScore;
        @Schema(description = "Submitted at") private LocalDateTime submittedAt;
    }
}
