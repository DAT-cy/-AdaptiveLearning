package adaptivelearning.module.speaking.repository;

import adaptivelearning.module.speaking.entity.SpeakingSubmission;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeakingSubmissionRepository extends JpaRepository<SpeakingSubmission, Long> {

    List<SpeakingSubmission> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<SpeakingSubmission> findByAttemptId(Long attemptId);

    Optional<SpeakingSubmission> findByIdAndUserId(Long id, Long userId);

    Optional<SpeakingSubmission> findBySubmissionIdAndUserId(Long submissionId, Long userId);
}
