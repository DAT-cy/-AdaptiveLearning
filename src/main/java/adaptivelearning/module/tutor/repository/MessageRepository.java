package adaptivelearning.module.tutor.repository;
import adaptivelearning.module.tutor.entity.TutorMessage;
import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface MessageRepository extends JpaRepository<TutorMessage, Long> {
    List<TutorMessage> findByConversationIdOrderByCreatedAtAsc(Long id);
    long countByConversationId(Long id);
}
