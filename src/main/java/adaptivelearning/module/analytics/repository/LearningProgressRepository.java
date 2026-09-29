package adaptivelearning.module.analytics.repository;
import adaptivelearning.module.analytics.entity.LearningProgress;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LearningProgressRepository extends JpaRepository<LearningProgress, Long> {
    List<LearningProgress> findByUserIdAndCertificateIdOrderBySnapshotAtDesc(Long userId, Long certId);
}
