package adaptivelearning.module.questions.repository;

import adaptivelearning.module.questions.entity.Question;
import adaptivelearning.shared.enums.ContentStatus;
import adaptivelearning.shared.enums.QuestionDifficulty;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Question} persistence operations.
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByCertificateIdAndStatus(Long certificateId, ContentStatus status);

    List<Question> findByTopicIdAndStatus(Long topicId, ContentStatus status);

    List<Question> findByCertificateIdAndTopicIdAndDifficultyAndStatus(
            Long certId, Long topicId, QuestionDifficulty diff, ContentStatus status);

    long countByCertificateIdAndStatus(Long certificateId, ContentStatus status);

    /**
     * Fetches a random sample of questions for the given certificate and status.
     * The supplied {@link Pageable} supplies the {@code LIMIT} for random sampling.
     */
    @Query(
            value = "SELECT * FROM questions q "
                    + "WHERE q.certificate_id = :certificateId AND q.status = :status "
                    + "ORDER BY RAND()",
            nativeQuery = true)
    List<Question> findByCertificateIdAndStatus(
            @Param("certificateId") Long certificateId,
            @Param("status") ContentStatus status,
            Pageable pageable);
}
