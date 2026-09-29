package adaptivelearning.module.topics.controller;

import adaptivelearning.config.response.DefaultRes;
import adaptivelearning.config.response.ResponseMessage;
import adaptivelearning.config.response.StatusCode;
import adaptivelearning.module.topics.dto.request.TopicRequest;
import adaptivelearning.module.topics.dto.response.TopicResponse;
import adaptivelearning.module.topics.service.TopicService;
import adaptivelearning.shared.enums.TopicType;
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
@RequestMapping(ClientUtils.VERSION + "/topics")
@Tag(name = "Topics", description = "Topic management")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping
    public ResponseEntity<DefaultRes<List<TopicResponse>>> getAll() {
        return ResponseEntity.ok(
                DefaultRes.res(StatusCode.OK, ResponseMessage.GET_LIST, topicService.getAll())
        );
    }

    @GetMapping("/certificate/{certificateId}")
    public ResponseEntity<DefaultRes<List<TopicResponse>>> getByCertificateId(@PathVariable Long certificateId) {
        return ResponseEntity.ok(
                DefaultRes.res(StatusCode.OK, ResponseMessage.GET_LIST, topicService.getByCertificateId(certificateId))
        );
    }

    @GetMapping("/certificate/{certificateId}/tree")
    public ResponseEntity<DefaultRes<List<TopicResponse>>> getTreeByCertificate(@PathVariable Long certificateId) {
        return ResponseEntity.ok(
                DefaultRes.res(StatusCode.OK, ResponseMessage.GET_LIST, topicService.getTreeByCertificate(certificateId))
        );
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<DefaultRes<List<TopicResponse>>> getByType(@PathVariable TopicType type) {
        return ResponseEntity.ok(
                DefaultRes.res(StatusCode.OK, ResponseMessage.GET_LIST, topicService.getByType(type))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DefaultRes<TopicResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                DefaultRes.res(StatusCode.OK, ResponseMessage.GET_ONE, topicService.getById(id))
        );
    }

    @GetMapping("/{id}/children")
    public ResponseEntity<DefaultRes<List<TopicResponse>>> getChildren(@PathVariable Long id) {
        return ResponseEntity.ok(
                DefaultRes.res(StatusCode.OK, ResponseMessage.GET_LIST, topicService.getChildren(id))
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DefaultRes<TopicResponse>> create(@Valid @RequestBody TopicRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED, topicService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DefaultRes<TopicResponse>> update(@PathVariable Long id,
                                                            @Valid @RequestBody TopicRequest request) {
        return ResponseEntity.ok(
                DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATED, topicService.update(id, request))
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DefaultRes<Void>> delete(@PathVariable Long id) {
        topicService.delete(id);
        return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, ResponseMessage.DELETED));
    }
}
