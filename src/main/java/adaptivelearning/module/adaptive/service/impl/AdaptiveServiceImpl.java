package adaptivelearning.module.adaptive.service.impl;

import adaptivelearning.config.response.CommonException;
import adaptivelearning.config.response.ErrorCode;
import adaptivelearning.module.adaptive.dto.response.AdaptiveRecommendationResponse;
import adaptivelearning.module.adaptive.dto.response.LearnerProfileResponse;
import adaptivelearning.module.adaptive.dto.response.ScoreForecastResponse;
import adaptivelearning.module.adaptive.entity.LearnerProfile;
import adaptivelearning.module.adaptive.repository.LearnerProfileRepository;
import adaptivelearning.module.adaptive.service.AdaptiveService;
import adaptivelearning.module.attempts.entity.ExamAttempt;
import adaptivelearning.module.attempts.repository.ExamAttemptRepository;
import adaptivelearning.module.exams.entity.ExamQuestion;
import adaptivelearning.module.exams.repository.ExamQuestionRepository;
import adaptivelearning.module.questions.entity.Question;
import adaptivelearning.module.questions.repository.QuestionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdaptiveServiceImpl implements AdaptiveService {

    private final LearnerProfileRepository profileRepository;
    private final ExamAttemptRepository attemptRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;

    @Override
    public LearnerProfileResponse getOrCreateProfile(Long userId, Long certificateId) {
        LearnerProfile profile = profileRepository.findByUserIdAndCertificateId(userId, certificateId)
                .orElseGet(() -> profileRepository.save(LearnerProfile.builder()
                        .userId(userId)
                        .certificateId(certificateId)
                        .abilityBySkillJson("{}")
                        .lastUpdated(LocalDateTime.now())
                        .totalAttempts(0)
                        .build()));
        return toResponse(profile);
    }

    @Override
    public LearnerProfileResponse updateProfile(Long userId, Long attemptId) {
        ExamAttempt attempt = attemptRepository.findById(attemptId)
                .filter(a -> userId.equals(a.getUserId()))
                .orElseThrow(() -> new CommonException(ErrorCode.ENTITY_NOT_FOUND));

        Long certificateId = resolveCertificateId(attempt);

        LearnerProfile profile = profileRepository.findByUserIdAndCertificateId(userId, certificateId)
                .orElseGet(() -> LearnerProfile.builder()
                        .userId(userId)
                        .certificateId(certificateId)
                        .abilityBySkillJson("{}")
                        .totalAttempts(0)
                        .build());

        Map<String, Double> abilityMap = parseAbilityJson(profile.getAbilityBySkillJson());

        // Compute score ratio from attempt
        double ratio = 0.0;
        if (attempt.getScore() != null && attempt.getMaxScore() != null && attempt.getMaxScore() > 0) {
            ratio = attempt.getScore() / attempt.getMaxScore();
        }

        // Update ability per skill/topic from exam questions
        List<ExamQuestion> examQuestions = examQuestionRepository.findByExamId(attempt.getExamId());
        for (ExamQuestion eq : examQuestions) {
            Question q = questionRepository.findById(eq.getQuestionId()).orElse(null);
            if (q != null) {
                String skillKey = "TOPIC_" + q.getTopicId();
                // Weighted moving average: new = (old + ratio) / 2
                abilityMap.merge(skillKey, ratio, (old, v) -> (old + v) / 2.0);
            }
        }

        profile.setAbilityBySkillJson(writeAbilityJson(abilityMap));
        profile.setTotalAttempts(profile.getTotalAttempts() + 1);
        profile.setLastUpdated(LocalDateTime.now());

        LearnerProfile saved = profileRepository.save(profile);
        log.info("Updated learner profile {} for user {} (attempt {})", saved.getProfileId(), userId, attemptId);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdaptiveRecommendationResponse> recommend(Long userId, Long certificateId) {
        LearnerProfile profile = profileRepository.findByUserIdAndCertificateId(userId, certificateId)
                .orElse(null);

        if (profile == null) {
            return List.of(new AdaptiveRecommendationResponse(
                    "GENERAL", "MEDIUM", 0.5, "No learner history yet"));
        }

        Map<String, Double> abilityMap = parseAbilityJson(profile.getAbilityBySkillJson());
        if (abilityMap.isEmpty()) {
            return List.of(new AdaptiveRecommendationResponse(
                    "GENERAL", "MEDIUM", 0.5, "No skill history yet"));
        }

        // Sort by lowest ability first, recommend target difficulty based on weakness
        return abilityMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(3)
                .map(entry -> {
                    double ability = entry.getValue();
                    String target;
                    if (ability < 0.4) {
                        target = "EASY";
                    } else if (ability < 0.7) {
                        target = "MEDIUM";
                    } else {
                        target = "HARD";
                    }
                    double confidence = 1.0 - Math.abs(0.5 - ability);
                    return new AdaptiveRecommendationResponse(
                            entry.getKey(), target, confidence,
                            "Based on lowest observed ability (" + String.format("%.2f", ability) + ")");
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ScoreForecastResponse forecast(Long userId, Long certificateId) {
        Optional<LearnerProfile> profileOpt =
                profileRepository.findByUserIdAndCertificateId(userId, certificateId);

        if (profileOpt.isEmpty()) {
            return new ScoreForecastResponse(
                    certificateId, 0.0, 0.2,
                    "No learner profile found for this certificate");
        }

        LearnerProfile profile = profileOpt.get();
        Map<String, Double> abilityMap = parseAbilityJson(profile.getAbilityBySkillJson());

        // Simple heuristic: average ability scaled to 0-990 range
        double avgAbility = abilityMap.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.5);

        double forecastScore = Math.round(avgAbility * 9900.0) / 100.0;
        double confidence = Math.min(1.0, Math.max(0.2, profile.getTotalAttempts() / 10.0));

        return new ScoreForecastResponse(
                certificateId, forecastScore, confidence,
                "Heuristic forecast from " + abilityMap.size() + " skill abilities across "
                        + profile.getTotalAttempts() + " attempts");
    }

    // ---- helpers ----

    private Long resolveCertificateId(ExamAttempt attempt) {
        List<ExamQuestion> examQuestions = examQuestionRepository.findByExamId(attempt.getExamId());
        if (examQuestions.isEmpty()) {
            throw new CommonException(ErrorCode.ENTITY_NOT_FOUND);
        }
        Long firstQuestionId = examQuestions.get(0).getQuestionId();
        return questionRepository.findById(firstQuestionId)
                .map(Question::getCertificateId)
                .orElseThrow(() -> new CommonException(ErrorCode.ENTITY_NOT_FOUND));
    }

    private Map<String, Double> parseAbilityJson(String json) {
        try {
            if (json == null || json.isBlank()) {
                return new HashMap<>();
            }
            return objectMapper.readValue(json, new TypeReference<Map<String, Double>>() {});
        } catch (Exception e) {
            log.warn("Failed to parse ability JSON: {}", e.getMessage());
            return new HashMap<>();
        }
    }

    private String writeAbilityJson(Map<String, Double> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
    }

    private LearnerProfileResponse toResponse(LearnerProfile p) {
        return LearnerProfileResponse.builder()
                .profileId(p.getProfileId())
                .userId(p.getUserId())
                .certificateId(p.getCertificateId())
                .abilityBySkillJson(p.getAbilityBySkillJson())
                .lastUpdated(p.getLastUpdated())
                .totalAttempts(p.getTotalAttempts())
                .build();
    }
}
