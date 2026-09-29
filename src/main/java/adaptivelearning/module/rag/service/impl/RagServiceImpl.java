package adaptivelearning.module.rag.service.impl;

import adaptivelearning.config.response.*; import adaptivelearning.module.rag.dto.request.*; import adaptivelearning.module.rag.dto.response.*; import adaptivelearning.module.rag.entity.*; import adaptivelearning.module.rag.repository.*; import adaptivelearning.module.rag.service.RagService; import lombok.RequiredArgsConstructor; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.*; import java.util.stream.*;

@Service @Transactional @RequiredArgsConstructor
public class RagServiceImpl implements RagService {
 private final StudyDocumentRepository documents; private final DocumentChunkRepository chunks;
 public StudyDocumentResponse uploadDocument(Long uid,DocumentUploadRequest r){ StudyDocument d=documents.save(StudyDocument.builder().userId(uid).certificateId(r.getCertificateId()).title(r.getTitle()).fileName(r.getTitle()).mimeType("text/markdown").status(adaptivelearning.shared.enums.DocumentStatus.READY).build()); List<DocumentChunk> cs=new ArrayList<>(); String text=r.getContent(); for(int i=0;i<text.length();i+=500){String part=text.substring(i,Math.min(i+500,text.length())); cs.add(DocumentChunk.builder().documentId(d.getDocumentId()).chunkIndex(cs.size()).content(part).keywords(keywords(part)).build());} chunks.saveAll(cs); d.setChunkCount(cs.size()); return to(documents.save(d)); }
 public List<StudyDocumentResponse> listDocuments(Long uid){return documents.findByUserIdOrderByDocCreatedAtDesc(uid).stream().map(this::to).toList();}
 public StudyDocumentResponse getDocument(Long uid,Long id){return to(owned(uid,id));}
 public SearchResponse search(Long uid,Long id,String q){owned(uid,id); String query=q==null?"":q.trim(); List<DocumentChunk> found=Arrays.stream(query.split("\\s+")).filter(s->!s.isBlank()).flatMap(k->chunks.search(id,k).stream()).distinct().toList(); return SearchResponse.builder().query(q).documentId(id).results(found.stream().map(this::chunk).toList()).build();}
 public void deleteDocument(Long uid,Long id){StudyDocument d=owned(uid,id); chunks.deleteByDocumentId(d.getDocumentId()); documents.delete(d);}
 private StudyDocument owned(Long uid,Long id){return documents.findById(id).filter(d->uid.equals(d.getUserId())).orElseThrow(()->new CommonException(ErrorCode.ENTITY_NOT_FOUND));}
 private String keywords(String s){return Arrays.stream(s.toLowerCase().split("[^\\p{L}\\p{Nd}]+" )).filter(w->w.length()>3).distinct().limit(30).collect(Collectors.joining(","));}
 private StudyDocumentResponse to(StudyDocument d){return StudyDocumentResponse.builder().documentId(d.getDocumentId()).title(d.getTitle()).fileName(d.getFileName()).status(d.getStatus()).chunkCount(d.getChunkCount()).certificateId(d.getCertificateId()).createdAt(d.getDocCreatedAt()).build();}
 private ChunkResponse chunk(DocumentChunk c){String s=c.getContent(); return ChunkResponse.builder().id(c.getId()).chunkIndex(c.getChunkIndex()).content(s.length()>300?s.substring(0,300):s).build();}
}

