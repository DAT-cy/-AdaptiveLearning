package adaptivelearning.module.attempts.repository;

import adaptivelearning.module.attempts.entity.AttemptEvent;
import adaptivelearning.shared.enums.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttemptEventRepository extends JpaRepository<AttemptEvent, Long> {

    List<AttemptEvent> findByAttemptId(Long attemptId);

    long countByAttemptIdAndEventType(Long attemptId, EventType eventType);
}
