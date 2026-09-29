package adaptivelearning.module.practice.service;
import adaptivelearning.module.practice.dto.request.PracticePackageRequest;
import adaptivelearning.module.practice.dto.response.PracticePackageResponse;
import java.util.List;
public interface PracticeService {
    List<PracticePackageResponse> getAll(Long certificateId);
    PracticePackageResponse getById(Long id);
    PracticePackageResponse create(PracticePackageRequest request);
    PracticePackageResponse update(Long id, PracticePackageRequest request);
    void delete(Long id);
}
