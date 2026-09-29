package adaptivelearning.module.exams.controller;

import adaptivelearning.config.response.DefaultRes;
import adaptivelearning.config.response.ResponseMessage;
import adaptivelearning.config.response.StatusCode;
import adaptivelearning.module.exams.dto.request.ExamQuestionRequest;
import adaptivelearning.module.exams.dto.request.ExamRequest;
import adaptivelearning.module.exams.dto.response.ExamDetailResponse;
import adaptivelearning.module.exams.dto.response.ExamQuestionResponse;
import adaptivelearning.module.exams.dto.response.ExamResponse;
import adaptivelearning.module.exams.service.ExamService;
import adaptivelearning.utils.ClientUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ClientUtils.VERSION + "/exams")
@Tag(name = "Exams", description = "Exam management")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @GetMapping
    @Operation(summary = "List exams", description = "Optionally filters by certificateId. Without filter, returns all exams.")
    public ResponseEntity<DefaultRes<List<ExamResponse>>> list(@RequestParam(required = false) Long certificateId) {
        List<ExamResponse> data = examService.getAll(certificateId);
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.GET_LIST, data));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get exam summary by id")
    public ResponseEntity<DefaultRes<ExamResponse>> get(@PathVariable Long id) {
        ExamResponse data = examService.getById(id);
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.GET_ONE, data));
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "Get exam detail including questions")
    public ResponseEntity<DefaultRes<ExamDetailResponse>> detail(@PathVariable Long id) {
        ExamDetailResponse data = examService.getDetail(id);
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.GET_ONE, data));
    }

    @PostMapping
    @Operation(summary = "Create a new exam")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DefaultRes<ExamResponse>> create(@Valid @RequestBody ExamRequest request) {
        ExamResponse data = examService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED, data));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an exam")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DefaultRes<ExamResponse>> update(@PathVariable Long id, @Valid @RequestBody ExamRequest request) {
        ExamResponse data = examService.update(id, request);
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATED, data));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an exam")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DefaultRes<Void>> delete(@PathVariable Long id) {
        examService.delete(id);
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.DELETED));
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "Publish an exam (DRAFT → PUBLISHED)")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DefaultRes<ExamResponse>> publish(@PathVariable Long id) {
        ExamResponse data = examService.publish(id);
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATED, data));
    }

    @PostMapping("/{id}/archive")
    @Operation(summary = "Archive an exam (PUBLISHED → ARCHIVED)")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DefaultRes<ExamResponse>> archive(@PathVariable Long id) {
        ExamResponse data = examService.archive(id);
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATED, data));
    }

    @PostMapping("/{id}/questions")
    @Operation(summary = "Add a question to an exam")
    public ResponseEntity<DefaultRes<ExamQuestionResponse>> addQuestion(
            @PathVariable Long id, @RequestBody ExamQuestionRequest request) {
        ExamQuestionResponse data = examService.addQuestion(id, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED, data));
    }

    @DeleteMapping("/{id}/questions/{questionId}")
    @Operation(summary = "Remove a question from an exam")
    public ResponseEntity<DefaultRes<Void>> removeQuestion(
            @PathVariable Long id, @PathVariable Long questionId) {
        examService.removeQuestion(id, questionId);
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.DELETED));
    }

    @PutMapping("/{id}/questions/reorder")
    @Operation(summary = "Reorder questions within an exam")
    public ResponseEntity<DefaultRes<List<ExamQuestionResponse>>> reorder(
            @PathVariable Long id, @RequestBody List<ExamQuestionRequest> requests) {
        List<ExamQuestionResponse> data = examService.reorderQuestions(id, requests);
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATED, data));
    }
}
