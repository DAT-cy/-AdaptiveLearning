package adaptivelearning.module.ai.repository;

import adaptivelearning.module.ai.entity.AiJob;
import adaptivelearning.shared.enums.AiJobStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiJobRepository extends JpaRepository<AiJob, Long> {

    Optional<AiJob> findByIdempotencyKey(String key);

    List<AiJob> findByStatus(AiJobStatus status);

    List<AiJob> findByInputRefTypeAndInputRefId(String type, Long id);
}
