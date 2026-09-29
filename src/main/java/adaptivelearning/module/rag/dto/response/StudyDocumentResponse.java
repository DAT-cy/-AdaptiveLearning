package adaptivelearning.module.rag.dto.response;
import adaptivelearning.shared.enums.DocumentStatus; import lombok.Builder; import lombok.Data; import java.time.LocalDateTime;
@Data @Builder public class StudyDocumentResponse { private Long documentId; private String title,fileName; private DocumentStatus status; private int chunkCount; private Long certificateId; private LocalDateTime createdAt; }
