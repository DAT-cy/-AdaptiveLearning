package adaptivelearning.module.exams.service;

import adaptivelearning.module.exams.dto.request.ExamQuestionRequest;
import adaptivelearning.module.exams.dto.request.ExamRequest;
import adaptivelearning.module.exams.dto.response.ExamDetailResponse;
import adaptivelearning.module.exams.dto.response.ExamQuestionResponse;
import adaptivelearning.module.exams.dto.response.ExamResponse;

import java.util.List;

public interface ExamService {

    List<ExamResponse> getAll(Long certificateId);

    ExamResponse getById(Long id);

    ExamDetailResponse getDetail(Long id);

    ExamResponse create(ExamRequest request);

    ExamResponse update(Long id, ExamRequest request);

    void delete(Long id);

    ExamResponse publish(Long id);

    ExamResponse archive(Long id);

    ExamQuestionResponse addQuestion(Long examId, ExamQuestionRequest request);

    void removeQuestion(Long examId, Long questionId);

    List<ExamQuestionResponse> reorderQuestions(Long examId, List<ExamQuestionRequest> requests);
}
