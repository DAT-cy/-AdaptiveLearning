package adaptivelearning.module.tutor.repository;
import adaptivelearning.module.tutor.entity.TutorConversation;
import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface ConversationRepository extends JpaRepository<TutorConversation, Long> {
    List<TutorConversation> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<TutorConversation> findByIdAndUserId(Long id, Long userId);
}
