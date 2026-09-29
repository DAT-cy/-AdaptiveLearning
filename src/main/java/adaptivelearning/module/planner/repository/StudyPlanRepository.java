package adaptivelearning.module.planner.repository;
import adaptivelearning.module.planner.entity.StudyPlan; import adaptivelearning.shared.enums.PlanStatus; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface StudyPlanRepository extends JpaRepository<StudyPlan,Long> { List<StudyPlan> findByUserIdAndStatus(Long userId, PlanStatus status); List<StudyPlan> findByUserIdOrderByPlanCreatedAtDesc(Long userId); List<StudyPlan> findByUserIdAndCertificateIdAndStatus(Long userId, Long certificateId, PlanStatus status); }
