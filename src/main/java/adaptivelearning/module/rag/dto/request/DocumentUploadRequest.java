package adaptivelearning.module.rag.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data public class DocumentUploadRequest { @NotBlank @Size(max=200) private String title; private Long certificateId; @NotBlank private String content; }
