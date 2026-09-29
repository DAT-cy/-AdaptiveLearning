package adaptivelearning.module.contentgen.controller;
import adaptivelearning.config.response.*; import adaptivelearning.module.contentgen.dto.request.GenerateQuestionsRequest; import adaptivelearning.module.contentgen.dto.response.ContentGenerationResponse; import adaptivelearning.module.contentgen.service.ContentGenerationService; import adaptivelearning.utils.*; import io.swagger.v3.oas.annotations.tags.Tag; import jakarta.validation.Valid; import java.util.*; import lombok.RequiredArgsConstructor; import org.springframework.http.*; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping(ClientUtils.VERSION+"/content-generation") @Tag(name="Content Generation") @RequiredArgsConstructor
public class ContentGenerationController { private final ContentGenerationService service; private Long user(){ try{return SecurityUtils.getCurrentUserId();}catch(Exception e){throw new CommonException(ErrorCode.FORBIDDEN_ERROR);} }
 @PostMapping @PreAuthorize("isAuthenticated()") public ResponseEntity<DefaultRes<?>> submit(@Valid @RequestBody GenerateQuestionsRequest r){return ResponseEntity.status(202).body(DefaultRes.res(202,"ACCEPTED",service.submit(user(),r)));}
 @GetMapping @PreAuthorize("isAuthenticated()") public DefaultRes<?> list(){return DefaultRes.res(200,"GET LIST",service.list(user()));}
 @GetMapping("/{id}") @PreAuthorize("isAuthenticated()") public DefaultRes<?> get(@PathVariable Long id){return DefaultRes.res(200,"GET ONE",service.get(user(),id));}
 @PostMapping("/{id}/apply") @PreAuthorize("hasRole('ADMIN')") public DefaultRes<?> apply(@PathVariable Long id){return DefaultRes.res(200,"UPDATED",service.apply(id,user()));}
}
