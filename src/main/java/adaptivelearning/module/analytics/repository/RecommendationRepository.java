package adaptivelearning.module.analytics.repository;
import adaptivelearning.module.analytics.entity.Recommendation;
import adaptivelearning.shared.enums.RecommendationStatus;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Recommendation> findByUserIdAndStatus(Long userId, RecommendationStatus status);
}
