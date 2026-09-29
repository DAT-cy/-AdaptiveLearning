package adaptivelearning.module.tutor.service;
import adaptivelearning.module.tutor.dto.request.*; import adaptivelearning.module.tutor.dto.response.*;
import java.util.*;
public interface TutorService { ConversationResponse createConversation(Long u, CreateConversationRequest r); List<ConversationResponse> getConversations(Long u); List<MessageResponse> getMessages(Long u, Long id); MessageResponse ask(Long u, Long id, AskTutorRequest r); void archive(Long u, Long id); }
