package adaptivelearning.module.questions.service;

import adaptivelearning.module.questions.dto.request.QuestionFilterRequest;
import adaptivelearning.module.questions.dto.request.QuestionRequest;
import adaptivelearning.module.questions.dto.response.QuestionResponse;
import adaptivelearning.shared.enums.QuestionDifficulty;
import java.util.List;

/**
 * Business operations for the question bank.
 */
public interface QuestionService {

    List<QuestionResponse> getAll(QuestionFilterRequest filter);

    QuestionResponse getById(Long questionId);

    QuestionResponse create(QuestionRequest request);

    QuestionResponse update(Long questionId, QuestionRequest request);

    QuestionResponse submitForReview(Long questionId);

    QuestionResponse approve(Long questionId);

    QuestionResponse reject(Long questionId);

    /**
     * Returns a random set of approved questions for a mock test.
     *
     * @param certificateId certificate scope
     * @param count         maximum number of questions to return
     * @param topicId       optional topic filter; {@code null} to ignore
     * @param difficulty    optional difficulty filter; {@code null} to ignore
     */
    List<QuestionResponse> getRandomQuestions(
            Long certificateId, int count, Long topicId, QuestionDifficulty difficulty);

    long countByCertificate(Long certificateId);
}
