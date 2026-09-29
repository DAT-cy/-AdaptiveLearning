package adaptivelearning.module.ai.repository;

import adaptivelearning.module.ai.entity.AiEvaluation;
import adaptivelearning.shared.enums.AiJobStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiEvaluationRepository extends JpaRepository<AiEvaluation, Long> {

    List<AiEvaluation> findByAttemptId(Long attemptId);

    Optional<AiEvaluation> findByAttemptIdAndQuestionId(Long attemptId, Long questionId);

    List<AiEvaluation> findByStatus(AiJobStatus status);
}
