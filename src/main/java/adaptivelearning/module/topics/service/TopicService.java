package adaptivelearning.module.topics.service;

import adaptivelearning.module.topics.dto.request.TopicRequest;
import adaptivelearning.module.topics.dto.response.TopicResponse;
import adaptivelearning.shared.enums.TopicType;

import java.util.List;

public interface TopicService {

    List<TopicResponse> getAll();

    TopicResponse getById(Long id);

    List<TopicResponse> getByCertificateId(Long certificateId);

    List<TopicResponse> getByType(TopicType type);

    List<TopicResponse> getChildren(Long parentId);

    TopicResponse create(TopicRequest request);

    TopicResponse update(Long id, TopicRequest request);

    void delete(Long id);

    List<TopicResponse> getTreeByCertificate(Long certificateId);
}
