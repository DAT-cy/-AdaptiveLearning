package adaptivelearning.module.certificates.controller;

import adaptivelearning.config.response.DefaultRes;
import adaptivelearning.config.response.ResponseMessage;
import adaptivelearning.config.response.StatusCode;
import adaptivelearning.module.certificates.dto.request.CertificateRequest;
import adaptivelearning.module.certificates.dto.response.CertificateResponse;
import adaptivelearning.module.certificates.service.CertificateService;
import adaptivelearning.utils.ClientUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ClientUtils.VERSION + "/certificates")
@Tag(name = "Certificates", description = "Certificate management")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    @GetMapping
    public ResponseEntity<DefaultRes<List<CertificateResponse>>> getAll() {
        return ResponseEntity.ok(
                DefaultRes.res(StatusCode.OK, ResponseMessage.GET_LIST, certificateService.getAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DefaultRes<CertificateResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                DefaultRes.res(StatusCode.OK, ResponseMessage.GET_ONE, certificateService.getById(id))
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DefaultRes<CertificateResponse>> create(@Valid @RequestBody CertificateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED, certificateService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DefaultRes<CertificateResponse>> update(@PathVariable Long id,
                                                                  @Valid @RequestBody CertificateRequest request) {
        return ResponseEntity.ok(
                DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATED, certificateService.update(id, request))
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DefaultRes<Void>> delete(@PathVariable Long id) {
        certificateService.delete(id);
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.DELETED));
    }
}
