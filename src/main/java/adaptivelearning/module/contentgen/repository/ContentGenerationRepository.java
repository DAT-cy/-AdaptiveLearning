package adaptivelearning.module.contentgen.repository;
import adaptivelearning.module.contentgen.entity.ContentGeneration; import adaptivelearning.shared.enums.GenerationStatus; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface ContentGenerationRepository extends JpaRepository<ContentGeneration,Long> { List<ContentGeneration> findByRequestedByOrderByCreatedAtDesc(Long userId); List<ContentGeneration> findByStatus(GenerationStatus status); }
