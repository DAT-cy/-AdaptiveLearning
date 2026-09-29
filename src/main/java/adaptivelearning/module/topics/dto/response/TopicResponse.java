package adaptivelearning.module.topics.dto.response;

import adaptivelearning.shared.enums.TopicType;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TopicResponse")
public class TopicResponse {

    private Long topicId;
    private String name;
    private String code;
    private String description;
    private TopicType type;
    private Long certificateId;
    private Long parentId;
    private int sortOrder;
    private boolean isActive;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TopicResponse> children;
}
