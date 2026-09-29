package adaptivelearning.module.contentgen.service;
import adaptivelearning.module.contentgen.dto.request.GenerateQuestionsRequest; import adaptivelearning.module.contentgen.dto.response.ContentGenerationResponse; import java.util.List;
public interface ContentGenerationService { ContentGenerationResponse submit(Long userId, GenerateQuestionsRequest request); ContentGenerationResponse get(Long userId, Long id); List<ContentGenerationResponse> list(Long userId); ContentGenerationResponse apply(Long id, Long userId); void processPending(); }
