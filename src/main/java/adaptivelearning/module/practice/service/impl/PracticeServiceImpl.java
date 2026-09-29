package adaptivelearning.module.practice.service.impl;

import adaptivelearning.config.response.CommonException;
import adaptivelearning.config.response.ErrorCode;
import adaptivelearning.module.practice.dto.request.PracticePackageRequest;
import adaptivelearning.module.practice.dto.response.PracticePackageResponse;
import adaptivelearning.module.practice.entity.PracticePackage;
import adaptivelearning.module.practice.repository.PracticePackageRepository;
import adaptivelearning.module.practice.service.PracticeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional @RequiredArgsConstructor
public class PracticeServiceImpl implements PracticeService {
    private final PracticePackageRepository repository;
    private final ObjectMapper objectMapper;

    @Override @Transactional(readOnly = true)
    public List<PracticePackageResponse> getAll(Long certificateId) {
        List<PracticePackage> packages = certificateId == null ? repository.findAll() : repository.findByCertificateIdAndIsActive(certificateId, true);
        return packages.stream().map(this::response).toList();
    }
    @Override @Transactional(readOnly = true)
    public PracticePackageResponse getById(Long id) { return response(find(id)); }
    @Override public PracticePackageResponse create(PracticePackageRequest r) { return response(repository.save(fromRequest(new PracticePackage(), r))); }
    @Override public PracticePackageResponse update(Long id, PracticePackageRequest r) { PracticePackage p = find(id); return response(repository.save(fromRequest(p, r))); }
    @Override public void delete(Long id) { PracticePackage p = find(id); p.setActive(false); repository.save(p); }

    private PracticePackage fromRequest(PracticePackage p, PracticePackageRequest r) {
        p.setTitle(r.getTitle()); p.setDescription(r.getDescription()); p.setCertificateId(r.getCertificateId()); p.setTargetTopicId(r.getTargetTopicId()); p.setTargetDifficulty(r.getTargetDifficulty()); p.setQuestionIdsJson(r.getQuestionIdsJson());
        if (r.getEstimatedMinutes() != null) p.setEstimatedMinutes(r.getEstimatedMinutes());
        try { JsonNode node = objectMapper.readTree(r.getQuestionIdsJson()); p.setQuestionCount(node.isArray() ? node.size() : 0); } catch (Exception e) { throw new CommonException(ErrorCode.BAD_REQUEST); }
        return p;
    }
    private PracticePackage find(Long id) { return repository.findById(id).orElseThrow(() -> new CommonException(ErrorCode.ENTITY_NOT_FOUND)); }
    private PracticePackageResponse response(PracticePackage p) { return PracticePackageResponse.builder().packageId(p.getPackageId()).title(p.getTitle()).description(p.getDescription()).certificateId(p.getCertificateId()).targetTopicId(p.getTargetTopicId()).targetDifficulty(p.getTargetDifficulty()).questionIdsJson(p.getQuestionIdsJson()).estimatedMinutes(p.getEstimatedMinutes()).questionCount(p.getQuestionCount()).isActive(p.isActive()).build(); }
}
