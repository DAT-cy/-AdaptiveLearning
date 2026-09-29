package adaptivelearning.module.rag.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "document_chunks") @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class DocumentChunk {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private Long documentId;
    @Column(nullable = false) private int chunkIndex;
    @Column(nullable = false, columnDefinition = "TEXT") private String content;
    @Column(columnDefinition = "TEXT") private String keywords;
    @Column(nullable = false) @Builder.Default private LocalDateTime createdAt = LocalDateTime.now();
}
