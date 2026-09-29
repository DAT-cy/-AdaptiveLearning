package adaptivelearning.module.rag.controller;
import adaptivelearning.config.response.*; import adaptivelearning.module.rag.dto.request.*; import adaptivelearning.module.rag.dto.response.*; import adaptivelearning.module.rag.service.RagService; import adaptivelearning.utils.*; import io.swagger.v3.oas.annotations.tags.Tag; import jakarta.validation.Valid; import lombok.RequiredArgsConstructor; import org.springframework.http.*; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping(ClientUtils.VERSION+"/rag") @RequiredArgsConstructor @Tag(name="Document RAG") @PreAuthorize("isAuthenticated()")
public class RagController { private final RagService service;
 @PostMapping("/documents") public ResponseEntity<DefaultRes<StudyDocumentResponse>> upload(@Valid @RequestBody DocumentUploadRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(DefaultRes.res(StatusCode.CREATED,"Document uploaded",service.uploadDocument(SecurityUtils.getCurrentUserId(),r)));}
 @GetMapping("/documents") public DefaultRes<List<StudyDocumentResponse>> list(){return DefaultRes.res(StatusCode.OK,"Documents retrieved",service.listDocuments(SecurityUtils.getCurrentUserId()));}
 @GetMapping("/documents/{id}") public DefaultRes<StudyDocumentResponse> get(@PathVariable Long id){return DefaultRes.res(StatusCode.OK,"Document retrieved",service.getDocument(SecurityUtils.getCurrentUserId(),id));}
 @GetMapping("/documents/{id}/search") public DefaultRes<SearchResponse> search(@PathVariable Long id,@RequestParam String query){return DefaultRes.res(StatusCode.OK,"Search completed",service.search(SecurityUtils.getCurrentUserId(),id,query));}
 @DeleteMapping("/documents/{id}") public DefaultRes<Void> delete(@PathVariable Long id){service.deleteDocument(SecurityUtils.getCurrentUserId(),id);return DefaultRes.res(StatusCode.OK,"Document deleted");}
}
