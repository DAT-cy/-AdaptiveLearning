package adaptivelearning.module.tutor.service.impl;

import adaptivelearning.config.response.*; import adaptivelearning.module.ai.gateway.AiProvider;
import adaptivelearning.module.tutor.dto.request.*; import adaptivelearning.module.tutor.dto.response.*;
import adaptivelearning.module.tutor.entity.*; import adaptivelearning.module.tutor.repository.*; import adaptivelearning.module.tutor.service.TutorService; import adaptivelearning.shared.enums.TutorRole;
import java.time.*; import java.util.*; import java.util.stream.Collectors; import lombok.RequiredArgsConstructor; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Transactional
public class TutorServiceImpl implements TutorService {
    private static final String MODEL = "gpt-4o-mini"; private final ConversationRepository conversations; private final MessageRepository messages; private final AiProvider ai;
    public ConversationResponse createConversation(Long u, CreateConversationRequest r) { TutorConversation c=new TutorConversation(); c.setUserId(u); c.setCertificateId(r.getCertificateId()); c.setTitle(r.getTitle()==null?"Tutor conversation":r.getTitle()); return map(conversations.save(c)); }
    @Transactional(readOnly=true) public List<ConversationResponse> getConversations(Long u){ return conversations.findByUserIdOrderByCreatedAtDesc(u).stream().map(this::map).toList(); }
    @Transactional(readOnly=true) public List<MessageResponse> getMessages(Long u,Long id){ owner(u,id); return messages.findByConversationIdOrderByCreatedAtAsc(id).stream().map(this::map).toList(); }
    public MessageResponse ask(Long u,Long id,AskTutorRequest r){ TutorConversation c=owner(u,id); if(!c.isActive()) throw new CommonException(ErrorCode.BAD_REQUEST); TutorMessage user=new TutorMessage(); user.setConversationId(id); user.setRole(TutorRole.USER); user.setContent(r.getContent()); user.setTokenCount(r.getContent().length()); messages.save(user); if(r.getContextJson()!=null)c.setContextJson(r.getContextJson());
        List<TutorMessage> history=messages.findByConversationIdOrderByCreatedAtAsc(id); int start=Math.max(0,history.size()-10); StringBuilder prompt=new StringBuilder("Provide a grounded, helpful tutoring answer. Use only supplied context and conversation; do not invent facts. Return a JSON object with fields answer (string) and citations (array of objects with source and quote).\n"); for(int i=start;i<history.size();i++) prompt.append(history.get(i).getRole()).append(": ").append(history.get(i).getContent()).append("\n");
        try { String raw=ai.chatCompletionSync("You are a careful educational tutor.",prompt.toString(),MODEL,1200); if(raw==null||raw.isBlank()) throw new RuntimeException(); TutorMessage out=new TutorMessage(); out.setConversationId(id); out.setRole(TutorRole.ASSISTANT); out.setContent(raw); out.setModel(MODEL); out.setTokenCount(raw.length()); return map(messages.save(out)); } catch(Exception e){ throw new CommonException(ErrorCode.BAD_REQUEST); }
    }
    public void archive(Long u,Long id){ TutorConversation c=owner(u,id); c.setActive(false); conversations.save(c); }
    private TutorConversation owner(Long u,Long id){ return conversations.findByIdAndUserId(id,u).orElseThrow(()->new CommonException(ErrorCode.ENTITY_NOT_FOUND)); }
    private ConversationResponse map(TutorConversation c){ return ConversationResponse.builder().id(c.getId()).userId(c.getUserId()).certificateId(c.getCertificateId()).title(c.getTitle()).isActive(c.isActive()).createdAt(c.getCreatedAt()==null?null:c.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).messageCount(messages.countByConversationId(c.getId())).build(); }
    private MessageResponse map(TutorMessage m){ return MessageResponse.builder().id(m.getId()).conversationId(m.getConversationId()).role(m.getRole()).content(m.getContent()).citationsJson(m.getCitationsJson()).model(m.getModel()).tokenCount(m.getTokenCount()).createdAt(m.getCreatedAt()).build(); }
}
