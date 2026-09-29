package adaptivelearning.module.speaking.repository;

import adaptivelearning.module.speaking.entity.SpeakingEvaluation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeakingEvaluationRepository extends JpaRepository<SpeakingEvaluation, Long> {

    List<SpeakingEvaluation> findBySubmissionId(Long submissionId);

    List<SpeakingEvaluation> findBySubmissionIdAndQuestionId(Long submissionId, Long questionId);
}
