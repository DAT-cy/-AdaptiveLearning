package adaptivelearning.module.ai.controller;

import adaptivelearning.config.response.DefaultRes;
import adaptivelearning.module.ai.dto.request.AiGradeRequest;
import adaptivelearning.module.ai.dto.request.RubricRequest;
import adaptivelearning.module.ai.dto.response.*;
import adaptivelearning.module.ai.service.AiService;
import adaptivelearning.utils.ClientUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping(ClientUtils.VERSION + "/ai") @RequiredArgsConstructor
@Tag(name = "AI", description = "AI grading and evaluation")
public class AiController {
    private final AiService service;
    @PostMapping("/grade") public ResponseEntity<DefaultRes<AiJobResponse>> grade(@Valid @RequestBody AiGradeRequest r) { return ResponseEntity.status(HttpStatus.ACCEPTED).body(DefaultRes.res(202, "Grade job submitted", service.submitGradeJob(r))); }
    @GetMapping("/evaluations/{id}") public ResponseEntity<DefaultRes<AiEvaluationResponse>> evaluation(@PathVariable Long id) { return ResponseEntity.ok(DefaultRes.res(200, "Success", service.getEvaluation(id))); }
    @GetMapping("/evaluations/attempt/{attemptId}") public ResponseEntity<DefaultRes<List<AiEvaluationResponse>>> evaluations(@PathVariable Long attemptId) { return ResponseEntity.ok(DefaultRes.res(200, "Success", service.getEvaluationByAttempt(attemptId))); }
    @PostMapping("/jobs/{id}/retry") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<DefaultRes<AiJobResponse>> retry(@PathVariable Long id) { return ResponseEntity.ok(DefaultRes.res(200, "Job retry scheduled", service.retryJob(id))); }
    @GetMapping("/jobs") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<DefaultRes<List<AiJobResponse>>> jobs() { return ResponseEntity.ok(DefaultRes.res(200, "Success", service.getJobs())); }
    @PostMapping("/rubrics") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<DefaultRes<RubricResponse>> createRubric(@Valid @RequestBody RubricRequest r) { return ResponseEntity.status(HttpStatus.CREATED).body(DefaultRes.res(201, "Created", service.createRubric(r))); }
    @PutMapping("/rubrics/{id}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<DefaultRes<RubricResponse>> updateRubric(@PathVariable Long id, @Valid @RequestBody RubricRequest r) { return ResponseEntity.ok(DefaultRes.res(200, "Updated", service.updateRubric(id, r))); }
    @GetMapping("/rubrics/certificate/{certificateId}") public ResponseEntity<DefaultRes<List<RubricResponse>>> rubrics(@PathVariable Long certificateId) { return ResponseEntity.ok(DefaultRes.res(200, "Success", service.getRubricsByCertificate(certificateId))); }
}
