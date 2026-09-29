package adaptivelearning.module.exams.repository;

import adaptivelearning.module.exams.entity.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {
    List<ExamQuestion> findByExamId(Long examId);
    Optional<ExamQuestion> findByExamIdAndQuestionId(Long examId, Long questionId);
    int countByExamId(Long examId);
    void deleteByExamId(Long examId);
}
