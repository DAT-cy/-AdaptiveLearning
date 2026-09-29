package adaptivelearning.module.rag.repository;
import adaptivelearning.module.rag.entity.DocumentChunk; import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param; import java.util.List;
public interface DocumentChunkRepository extends JpaRepository<DocumentChunk,Long> {
 List<DocumentChunk> findByDocumentIdOrderByChunkIndexAsc(Long documentId);
 @Query("select c from DocumentChunk c where c.documentId = :documentId and lower(c.content) like lower(concat('%', :keyword, '%'))") List<DocumentChunk> search(@Param("documentId") Long documentId,@Param("keyword") String keyword);
 List<DocumentChunk> findByDocumentIdAndContentContaining(Long documentId,String content);
 void deleteByDocumentId(Long documentId);
}
