package adaptivelearning.module.ai.repository;

import adaptivelearning.module.ai.entity.Rubric;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RubricRepository extends JpaRepository<Rubric, Long> {

    Optional<Rubric> findByCertificateIdAndSkillAndIsActive(Long certificateId, String skill, boolean isActive);

    List<Rubric> findByCertificateId(Long certificateId);
}
