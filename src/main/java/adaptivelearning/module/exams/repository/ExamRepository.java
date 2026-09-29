package adaptivelearning.module.exams.repository;

import adaptivelearning.module.exams.entity.Exam;
import adaptivelearning.shared.enums.ContentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByCertificateIdAndStatus(Long certificateId, ContentStatus status);
    List<Exam> findByStatus(ContentStatus status);
}
