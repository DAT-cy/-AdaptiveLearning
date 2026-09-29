package adaptivelearning.module.practice.controller;

import adaptivelearning.config.response.DefaultRes;
import adaptivelearning.module.practice.dto.request.PracticePackageRequest;
import adaptivelearning.module.practice.dto.response.PracticePackageResponse;
import adaptivelearning.module.practice.service.PracticeService;
import adaptivelearning.utils.ClientUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping(ClientUtils.VERSION + "/practice") @RequiredArgsConstructor
@Tag(name = "Practice", description = "Practice packages")
public class PracticeController {
    private final PracticeService service;
    @GetMapping public ResponseEntity<DefaultRes<List<PracticePackageResponse>>> all(@RequestParam(required = false) Long certificateId) { return ResponseEntity.ok(DefaultRes.res(200, "Success", service.getAll(certificateId))); }
    @GetMapping("/{id}") public ResponseEntity<DefaultRes<PracticePackageResponse>> get(@PathVariable Long id) { return ResponseEntity.ok(DefaultRes.res(200, "Success", service.getById(id))); }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<DefaultRes<PracticePackageResponse>> create(@Valid @RequestBody PracticePackageRequest r) { return ResponseEntity.status(HttpStatus.CREATED).body(DefaultRes.res(201, "Created", service.create(r))); }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<DefaultRes<PracticePackageResponse>> update(@PathVariable Long id, @Valid @RequestBody PracticePackageRequest r) { return ResponseEntity.ok(DefaultRes.res(200, "Updated", service.update(id, r))); }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<DefaultRes<Void>> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.ok(DefaultRes.res(200, "Deleted", null)); }
}
