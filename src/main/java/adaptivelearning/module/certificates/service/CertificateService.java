package adaptivelearning.module.certificates.service;

import adaptivelearning.module.certificates.dto.request.CertificateRequest;
import adaptivelearning.module.certificates.dto.response.CertificateResponse;

import java.util.List;

public interface CertificateService {

    List<CertificateResponse> getAll();

    CertificateResponse getById(Long id);

    CertificateResponse create(CertificateRequest request);

    CertificateResponse update(Long id, CertificateRequest request);

    void delete(Long id);
}
