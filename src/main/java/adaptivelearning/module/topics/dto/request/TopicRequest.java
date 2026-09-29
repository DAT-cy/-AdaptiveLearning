package adaptivelearning.module.topics.dto.request;

import adaptivelearning.shared.enums.TopicType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TopicRequest")
public class TopicRequest {

    private String name;
    private String code;
    private String description;
    private TopicType type;
    private Long certificateId;
    private Long parentId;
    private int sortOrder;
}
