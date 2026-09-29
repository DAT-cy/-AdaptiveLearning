package adaptivelearning.module.speaking.controller;

import adaptivelearning.config.response.CommonException;
import adaptivelearning.config.response.DefaultRes;
import adaptivelearning.config.response.ErrorCode;
import adaptivelearning.module.speaking.dto.request.SpeakingEvaluationRequest;
import adaptivelearning.module.speaking.dto.request.SpeakingSubmissionRequest;
import adaptivelearning.module.speaking.dto.response.SpeakingEvaluationResponse;
import adaptivelearning.module.speaking.dto.response.SpeakingSubmissionResponse;
import adaptivelearning.module.speaking.service.SpeakingService;
import adaptivelearning.utils.ClientUtils;
import adaptivelearning.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ClientUtils.VERSION + "/speaking")
@RequiredArgsConstructor
@Tag(name = "Speaking", description = "Speaking submission and evaluation endpoints")
@PreAuthorize("isAuthenticated()")
public class SpeakingController {

    private final SpeakingService service;

    @PostMapping("/submissions")
    @Operation(summary = "Submit a speaking response (MVP: text transcript)")
    public ResponseEntity<DefaultRes<SpeakingSubmissionResponse>> submit(
            @Valid @RequestBody SpeakingSubmissionRequest request) {
        SpeakingSubmissionResponse data = service.submit(SecurityUtils.getCurrentUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DefaultRes.res(201, "Created", data));
    }

    @GetMapping("/submissions/{id}")
    @Operation(summary = "Get a speaking submission by id")
    public ResponseEntity<DefaultRes<SpeakingSubmissionResponse>> getSubmission(@PathVariable Long id) {
        SpeakingSubmissionResponse data = service.getSubmission(SecurityUtils.getCurrentUserId(), id);
        return ResponseEntity.ok(DefaultRes.res(200, "Success", data));
    }

    @GetMapping("/submissions/attempt/{attemptId}")
    @Operation(summary = "List speaking submissions for an attempt")
    public ResponseEntity<DefaultRes<List<SpeakingSubmissionResponse>>> listByAttempt(
            @PathVariable Long attemptId) {
        List<SpeakingSubmissionResponse> data = service.listByAttempt(
                SecurityUtils.getCurrentUserId(), attemptId);
        return ResponseEntity.ok(DefaultRes.res(200, "Success", data));
    }

    @PostMapping("/evaluations")
    @Operation(summary = "Evaluate a speaking submission via AI")
    public ResponseEntity<DefaultRes<SpeakingEvaluationResponse>> evaluate(
            @Valid @RequestBody SpeakingEvaluationRequest request) {
        SpeakingEvaluationResponse data = service.evaluate(
                SecurityUtils.getCurrentUserId(), request.getSubmissionId(), request.getRubricId());
        return ResponseEntity.ok(DefaultRes.res(200, "Success", data));
    }

    @GetMapping("/evaluations/{id}")
    @Operation(summary = "Get an evaluation by id")
    public ResponseEntity<DefaultRes<SpeakingEvaluationResponse>> getEvaluation(@PathVariable Long id) {
        SpeakingEvaluationResponse data = service.listEvaluations(id).stream()
                .findFirst()
                .orElseThrow(() -> new CommonException(ErrorCode.ENTITY_NOT_FOUND));
        return ResponseEntity.ok(DefaultRes.res(200, "Success", data));
    }

    @GetMapping("/evaluations/submission/{submissionId}")
    @Operation(summary = "List evaluations for a submission")
    public ResponseEntity<DefaultRes<List<SpeakingEvaluationResponse>>> listEvaluations(
            @PathVariable Long submissionId) {
        List<SpeakingEvaluationResponse> data = service.listEvaluations(submissionId);
        return ResponseEntity.ok(DefaultRes.res(200, "Success", data));
    }
}
