package adaptivelearning.module.analytics.repository;
import adaptivelearning.module.analytics.entity.GapAnalysis;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GapAnalysisRepository extends JpaRepository<GapAnalysis, Long> {
    Optional<GapAnalysis> findByAttemptId(Long attemptId);
    List<GapAnalysis> findByUserIdAndCertificateIdOrderByGeneratedAtDesc(Long userId, Long certId);
}
