package adaptivelearning.module.rag.dto.response;
import java.util.List; import lombok.Builder; import lombok.Data;
@Data @Builder public class SearchResponse { private String query; private List<ChunkResponse> results; private Long documentId; }
