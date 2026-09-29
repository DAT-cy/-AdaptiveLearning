package adaptivelearning.module.rag.entity;

import adaptivelearning.config.app.BaseEntity;
import adaptivelearning.shared.enums.DocumentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "study_documents") @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class StudyDocument extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long documentId;
    @Column(nullable = false) private Long userId;
    private Long certificateId;
    @NotBlank @Size(max = 200) @Column(nullable = false, length = 200) private String title;
    private String fileName; private String storagePath; private String mimeType;
    @Enumerated(EnumType.STRING) @Builder.Default @Column(nullable = false) private DocumentStatus status = DocumentStatus.UPLOADED;
    @Builder.Default @Column(name = "chunk_count") private int chunkCount = 0;
    @Column(name = "doc_created_at", nullable = false) @Builder.Default private LocalDateTime docCreatedAt = LocalDateTime.now();
}
