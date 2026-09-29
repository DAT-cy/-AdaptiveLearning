package adaptivelearning.module.attempts.controller;

import adaptivelearning.config.response.CommonException;
import adaptivelearning.config.response.DefaultRes;
import adaptivelearning.config.response.ErrorCode;
import adaptivelearning.config.response.ResponseMessage;
import adaptivelearning.config.response.StatusCode;
import adaptivelearning.module.attempts.dto.request.SaveAnswerRequest;
import adaptivelearning.module.attempts.dto.request.StartAttemptRequest;
import adaptivelearning.module.attempts.dto.request.SubmitAttemptRequest;
import adaptivelearning.module.attempts.dto.request.ViolationRequest;
import adaptivelearning.module.attempts.dto.response.AttemptDetailResponse;
import adaptivelearning.module.attempts.dto.response.AttemptResponse;
import adaptivelearning.module.attempts.dto.response.AttemptResultResponse;
import adaptivelearning.module.attempts.service.AttemptService;
import adaptivelearning.module.users.entity.Role;
import adaptivelearning.utils.ClientUtils;
import adaptivelearning.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ClientUtils.VERSION + "/attempts")
@Tag(name = "Attempts", description = "Exam attempt management")
@RequiredArgsConstructor
public class AttemptController {

    private final AttemptService attemptService;

    @PostMapping("/start")
    @Operation(summary = "Start an exam attempt for the authenticated user")
    public ResponseEntity<DefaultRes<AttemptResponse>> start(@Valid @RequestBody StartAttemptRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        AttemptResponse data = attemptService.startAttempt(userId, request.getExamId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED, data));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get attempt detail", description = "Accessible by the owning user or an ADMIN.")
    public ResponseEntity<DefaultRes<AttemptDetailResponse>> getAttempt(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        AttemptDetailResponse data = attemptService.getAttempt(id);
        if (!data.getUserId().equals(userId) && !SecurityUtils.hasRole(Role.ROLE_ADMIN)) {
            throw new CommonException(ErrorCode.FORBIDDEN_ERROR);
        }
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.GET_ONE, data));
    }

    @PatchMapping("/{id}/answers")
    @Operation(summary = "Save an answer (autosave)", description = "Owner-only autosave endpoint; upserts the answer for the question.")
    public ResponseEntity<DefaultRes<Void>> saveAnswer(
            @PathVariable Long id, @Valid @RequestBody SaveAnswerRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        attemptService.saveAnswer(id, userId, request);
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATED));
    }

    @PostMapping("/{id}/submit")
    @Operation(summary = "Submit an attempt", description = "Owner-only; final answers may be sent in the body and trigger auto-grading.")
    public ResponseEntity<DefaultRes<AttemptResultResponse>> submit(
            @PathVariable Long id, @RequestBody(required = false) SubmitAttemptRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        SubmitAttemptRequest body = (request != null) ? request : new SubmitAttemptRequest();
        AttemptResultResponse data = attemptService.submitAttempt(id, userId, body);
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATED, data));
    }

    @GetMapping("/{id}/result")
    @Operation(summary = "Get attempt result", description = "Accessible by the owning user or an ADMIN.")
    public ResponseEntity<DefaultRes<AttemptResultResponse>> getResult(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        AttemptDetailResponse detail = attemptService.getAttempt(id);
        if (!detail.getUserId().equals(userId) && !SecurityUtils.hasRole(Role.ROLE_ADMIN)) {
            throw new CommonException(ErrorCode.FORBIDDEN_ERROR);
        }
        AttemptResultResponse data = attemptService.getResult(id);
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.GET_ONE, data));
    }

    @PostMapping("/{id}/violations")
    @Operation(summary = "Record a violation event for an attempt")
    public ResponseEntity<DefaultRes<Void>> recordViolation(
            @PathVariable Long id, @RequestBody ViolationRequest request) {
        attemptService.recordViolation(id, request);
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.SUCCESS));
    }

    @GetMapping("/me/history")
    @Operation(summary = "Get the authenticated user's attempt history")
    public ResponseEntity<DefaultRes<List<AttemptResponse>>> getHistory() {
        Long userId = SecurityUtils.getCurrentUserId();
        List<AttemptResponse> data = attemptService.getHistory(userId);
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.GET_LIST, data));
    }
}
