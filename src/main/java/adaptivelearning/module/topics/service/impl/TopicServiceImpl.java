package adaptivelearning.module.topics.service.impl;

import adaptivelearning.config.response.CommonException;
import adaptivelearning.config.response.ErrorCode;
import adaptivelearning.module.topics.dto.request.TopicRequest;
import adaptivelearning.module.topics.dto.response.TopicResponse;
import adaptivelearning.module.topics.entity.Topic;
import adaptivelearning.module.topics.repository.TopicRepository;
import adaptivelearning.module.topics.service.TopicService;
import adaptivelearning.shared.enums.TopicType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    @Override
    public List<TopicResponse> getAll() {
        return topicRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public TopicResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    public List<TopicResponse> getByCertificateId(Long certificateId) {
        return topicRepository.findByCertificateId(certificateId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<TopicResponse> getByType(TopicType type) {
        return topicRepository.findAll().stream()
                .filter(topic -> topic.getType() == type)
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<TopicResponse> getChildren(Long parentId) {
        return topicRepository.findByParentId(parentId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public TopicResponse create(TopicRequest request) {
        Topic topic = toEntity(request);
        Topic saved = topicRepository.save(topic);
        log.info("Created topic: {}", saved.getName());
        return toResponse(saved);
    }

    @Override
    public TopicResponse update(Long id, TopicRequest request) {
        Topic topic = findOrThrow(id);
        applyFields(topic, request);
        Topic saved = topicRepository.save(topic);
        log.info("Updated topic id={}", saved.getTopicId());
        return toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Topic topic = findOrThrow(id);
        topic.setActive(false);
        topicRepository.save(topic);
        log.info("Soft-deleted topic id={}", id);
    }

    @Override
    public List<TopicResponse> getTreeByCertificate(Long certificateId) {
        List<TopicResponse> allTopics = topicRepository.findByCertificateId(certificateId).stream()
                .map(this::toResponse)
                .toList();

        Map<Long, TopicResponse> topicMap = allTopics.stream()
                .collect(Collectors.toMap(TopicResponse::getTopicId, Function.identity()));

        List<TopicResponse> roots = new ArrayList<>();

        for (TopicResponse topic : allTopics) {
            if (topic.getParentId() == null) {
                roots.add(topic);
            } else {
                TopicResponse parent = topicMap.get(topic.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(topic);
                }
            }
        }

        return roots;
    }

    private Topic findOrThrow(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.ENTITY_NOT_FOUND));
    }

    private Topic toEntity(TopicRequest request) {
        Topic topic = new Topic();
        applyFields(topic, request);
        return topic;
    }

    private void applyFields(Topic topic, TopicRequest request) {
        topic.setName(request.getName());
        topic.setCode(request.getCode());
        topic.setDescription(request.getDescription());
        topic.setType(request.getType());
        topic.setCertificateId(request.getCertificateId());
        topic.setParentId(request.getParentId());
        topic.setSortOrder(request.getSortOrder());
    }

    private TopicResponse toResponse(Topic topic) {
        return TopicResponse.builder()
                .topicId(topic.getTopicId())
                .name(topic.getName())
                .code(topic.getCode())
                .description(topic.getDescription())
                .type(topic.getType())
                .certificateId(topic.getCertificateId())
                .parentId(topic.getParentId())
                .sortOrder(topic.getSortOrder())
                .isActive(topic.isActive())
                .build();
    }
}
