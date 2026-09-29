package adaptivelearning.module.attempts.repository;

import adaptivelearning.module.attempts.entity.ExamAttempt;
import adaptivelearning.shared.enums.AttemptStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {

    Optional<ExamAttempt> findByUserIdAndExamId(Long userId, Long examId);

    List<ExamAttempt> findByUserId(Long userId);

    List<ExamAttempt> findByStatus(AttemptStatus status);

    List<ExamAttempt> findByUserIdOrderByStartedAtDesc(Long userId);
}
