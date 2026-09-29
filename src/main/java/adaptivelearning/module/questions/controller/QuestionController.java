package adaptivelearning.module.questions.controller;

import adaptivelearning.config.response.DefaultRes;
import adaptivelearning.module.questions.dto.request.QuestionFilterRequest;
import adaptivelearning.module.questions.dto.request.QuestionRequest;
import adaptivelearning.module.questions.dto.response.QuestionResponse;
import adaptivelearning.module.questions.service.QuestionService;
import adaptivelearning.shared.enums.QuestionDifficulty;
import adaptivelearning.utils.ClientUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for question bank management.
 */
@RestController
@RequestMapping(ClientUtils.VERSION + "/questions")
@RequiredArgsConstructor
@Tag(name = "Questions", description = "Question bank management")
@SecurityRequirement(name = "bearerAuth")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/random")
    @Operation(summary = "Get random approved questions for a test")
    public ResponseEntity<DefaultRes<List<QuestionResponse>>> getRandomQuestions(
            @Parameter(description = "Certificate ID", required = true, example = "1")
            @RequestParam Long certificateId,
            @Parameter(description = "Number of questions to return", required = true, example = "20")
            @RequestParam int count,
            @Parameter(description = "Optional topic filter", example = "10")
            @RequestParam(required = false) Long topicId,
            @Parameter(description = "Optional difficulty filter", example = "MEDIUM")
            @RequestParam(required = false) QuestionDifficulty difficulty) {
        List<QuestionResponse> questions =
                questionService.getRandomQuestions(certificateId, count, topicId, difficulty);
        return ResponseEntity.ok(DefaultRes.res(HttpStatus.OK.value(), "Success", questions));
    }

    @GetMapping
    @Operation(summary = "List questions with optional filters")
    public ResponseEntity<DefaultRes<List<QuestionResponse>>> getAll(
            QuestionFilterRequest filter) {
        List<QuestionResponse> questions = questionService.getAll(filter);
        return ResponseEntity.ok(DefaultRes.res(HttpStatus.OK.value(), "Success", questions));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a question by ID")
    public ResponseEntity<DefaultRes<QuestionResponse>> getById(@PathVariable Long id) {
        QuestionResponse question = questionService.getById(id);
        return ResponseEntity.ok(DefaultRes.res(HttpStatus.OK.value(), "Success", question));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CONTENT_EDITOR')")
    @Operation(summary = "Create a question", description = "Requires ADMIN or CONTENT_EDITOR role")
    public ResponseEntity<DefaultRes<QuestionResponse>> create(
            @Valid @RequestBody QuestionRequest request) {
        QuestionResponse created = questionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DefaultRes.res(HttpStatus.CREATED.value(), "Question created", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CONTENT_EDITOR')")
    @Operation(
            summary = "Update a question",
            description = "Requires ADMIN or CONTENT_EDITOR role")
    public ResponseEntity<DefaultRes<QuestionResponse>> update(
            @PathVariable Long id, @Valid @RequestBody QuestionRequest request) {
        QuestionResponse updated = questionService.update(id, request);
        return ResponseEntity.ok(DefaultRes.res(HttpStatus.OK.value(), "Question updated", updated));
    }

    @PostMapping("/{id}/submit-review")
    @Operation(summary = "Submit a draft question for review")
    public ResponseEntity<DefaultRes<QuestionResponse>> submitForReview(@PathVariable Long id) {
        QuestionResponse updated = questionService.submitForReview(id);
        return ResponseEntity.ok(
                DefaultRes.res(HttpStatus.OK.value(), "Question submitted for review", updated));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Approve a question in review", description = "Requires ADMIN role")
    public ResponseEntity<DefaultRes<QuestionResponse>> approve(@PathVariable Long id) {
        QuestionResponse updated = questionService.approve(id);
        return ResponseEntity.ok(DefaultRes.res(HttpStatus.OK.value(), "Question approved", updated));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Reject a question back to draft",
            description = "Requires ADMIN role")
    public ResponseEntity<DefaultRes<QuestionResponse>> reject(@PathVariable Long id) {
        QuestionResponse updated = questionService.reject(id);
        return ResponseEntity.ok(DefaultRes.res(HttpStatus.OK.value(), "Question rejected", updated));
    }

}
