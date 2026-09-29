package adaptivelearning.module.tutor.controller;
import adaptivelearning.config.response.*; import adaptivelearning.module.tutor.dto.request.*; import adaptivelearning.module.tutor.service.TutorService; import adaptivelearning.utils.*; import io.swagger.v3.oas.annotations.tags.Tag; import jakarta.validation.Valid; import java.util.*; import lombok.RequiredArgsConstructor; import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping(ClientUtils.VERSION+"/tutor") @Tag(name="AI Tutor") @RequiredArgsConstructor
public class TutorController { private final TutorService service; private Long user(){ try{return SecurityUtils.getCurrentUserId();}catch(Exception e){throw new CommonException(ErrorCode.FORBIDDEN_ERROR);} }
 @PostMapping("/conversations") public ResponseEntity<DefaultRes<?>> create(@RequestBody CreateConversationRequest r){return ResponseEntity.status(201).body(DefaultRes.res(201,"CREATED",service.createConversation(user(),r)));}
 @GetMapping("/conversations") public DefaultRes<?> list(){return DefaultRes.res(200,"GET LIST",service.getConversations(user()));}
 @GetMapping("/conversations/{id}/messages") public DefaultRes<?> messages(@PathVariable Long id){return DefaultRes.res(200,"GET LIST",service.getMessages(user(),id));}
 @PostMapping("/conversations/{id}/messages") public DefaultRes<?> ask(@PathVariable Long id,@Valid @RequestBody AskTutorRequest r){return DefaultRes.res(200,"SUCCESS",service.ask(user(),id,r));}
 @DeleteMapping("/conversations/{id}") public DefaultRes<Void> archive(@PathVariable Long id){service.archive(user(),id);return DefaultRes.res(200,"DELETED");}
}
