package adaptivelearning.module.adaptive.repository;

import adaptivelearning.module.adaptive.entity.LearnerProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearnerProfileRepository extends JpaRepository<LearnerProfile, Long> {

    Optional<LearnerProfile> findByUserIdAndCertificateId(Long userId, Long certificateId);
}
