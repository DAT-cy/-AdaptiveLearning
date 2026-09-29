package adaptivelearning.module.certificates.service.impl;

import adaptivelearning.config.response.CommonException;
import adaptivelearning.config.response.ErrorCode;
import adaptivelearning.module.certificates.dto.request.CertificateRequest;
import adaptivelearning.module.certificates.dto.response.CertificateResponse;
import adaptivelearning.module.certificates.entity.Certificate;
import adaptivelearning.module.certificates.repository.CertificateRepository;
import adaptivelearning.module.certificates.service.CertificateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;

    @Override
    public List<CertificateResponse> getAll() {
        return certificateRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CertificateResponse getById(Long id) {
        Certificate certificate = findOrThrow(id);
        return toResponse(certificate);
    }

    @Override
    public CertificateResponse create(CertificateRequest request) {
        Certificate certificate = toEntity(request);
        Certificate saved = certificateRepository.save(certificate);
        log.info("Created certificate: {}", saved.getName());
        return toResponse(saved);
    }

    @Override
    public CertificateResponse update(Long id, CertificateRequest request) {
        Certificate certificate = findOrThrow(id);
        certificate.setName(request.getName());
        certificate.setCode(request.getCode());
        certificate.setDescription(request.getDescription());
        certificate.setTotalScore(request.getTotalScore());
        certificate.setPassingScore(request.getPassingScore());
        certificate.setPassingBand(request.getPassingBand());
        certificate.setSkills(request.getSkills());
        Certificate saved = certificateRepository.save(certificate);
        log.info("Updated certificate id={}", saved.getCertificateId());
        return toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Certificate certificate = findOrThrow(id);
        certificateRepository.delete(certificate);
        log.info("Deleted certificate id={}", id);
    }

    private Certificate findOrThrow(Long id) {
        return certificateRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.ENTITY_NOT_FOUND));
    }

    private Certificate toEntity(CertificateRequest request) {
        return Certificate.builder()
                .name(request.getName())
                .code(request.getCode())
                .description(request.getDescription())
                .totalScore(request.getTotalScore())
                .passingScore(request.getPassingScore())
                .passingBand(request.getPassingBand())
                .skills(request.getSkills())
                .build();
    }

    private CertificateResponse toResponse(Certificate certificate) {
        return CertificateResponse.builder()
                .certificateId(certificate.getCertificateId())
                .name(certificate.getName())
                .code(certificate.getCode())
                .description(certificate.getDescription())
                .totalScore(certificate.getTotalScore())
                .passingScore(certificate.getPassingScore())
                .passingBand(certificate.getPassingBand())
                .skills(certificate.getSkills())
                .status(certificate.getStatus())
                .build();
    }
}
