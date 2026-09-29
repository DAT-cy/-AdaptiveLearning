package adaptivelearning.module.rag.service;
import adaptivelearning.module.rag.dto.request.DocumentUploadRequest; import adaptivelearning.module.rag.dto.response.*; import java.util.List;
public interface RagService { StudyDocumentResponse uploadDocument(Long userId,DocumentUploadRequest request); List<StudyDocumentResponse> listDocuments(Long userId); StudyDocumentResponse getDocument(Long userId,Long id); SearchResponse search(Long userId,Long documentId,String query); void deleteDocument(Long userId,Long id); }
