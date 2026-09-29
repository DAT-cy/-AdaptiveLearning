package adaptivelearning.module.practice.repository;
import adaptivelearning.module.practice.entity.PracticePackage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PracticePackageRepository extends JpaRepository<PracticePackage, Long> {
    List<PracticePackage> findByCertificateIdAndIsActive(Long certificateId, boolean isActive);
    List<PracticePackage> findByTargetTopicId(Long topicId);
}
