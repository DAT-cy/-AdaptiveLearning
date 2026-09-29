package adaptivelearning.module.analytics.service.impl;

import adaptivelearning.config.response.CommonException;
import adaptivelearning.config.response.ErrorCode;
import adaptivelearning.module.analytics.dto.response.GapAnalysisResponse;
import adaptivelearning.module.analytics.dto.response.ProgressResponse;
import adaptivelearning.module.analytics.dto.response.RecommendationResponse;
import adaptivelearning.module.analytics.entity.GapAnalysis;
import adaptivelearning.module.analytics.entity.LearningProgress;
import adaptivelearning.module.analytics.entity.Recommendation;
import adaptivelearning.module.analytics.repository.GapAnalysisRepository;
import adaptivelearning.module.analytics.repository.LearningProgressRepository;
import adaptivelearning.module.analytics.repository.RecommendationRepository;
import adaptivelearning.module.analytics.service.AnalyticsService;
import adaptivelearning.module.attempts.entity.AttemptAnswer;
import adaptivelearning.module.attempts.entity.ExamAttempt;
import adaptivelearning.module.attempts.repository.AttemptAnswerRepository;
import adaptivelearning.module.attempts.repository.ExamAttemptRepository;
import adaptivelearning.shared.enums.RecommendationStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {

    private static final double THRESHOLD_STRONG = 80.0;
    private static final double THRESHOLD_DEVELOPING = 60.0;
    private static final double THRESHOLD_WEAK = 40.0;

    private final AttemptAnswerRepository answerRepository;
    private final ExamAttemptRepository attemptRepository;
    private final GapAnalysisRepository gapAnalysisRepository;
    private final RecommendationRepository recommendationRepository;
    private final LearningProgressRepository progressRepository;
    private final ObjectMapper objectMapper;

    @Override
    public GapAnalysisResponse analyzeAttempt(Long attemptId) {
        ExamAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new CommonException(ErrorCode.ENTITY_NOT_FOUND));

        List<AttemptAnswer> answers = answerRepository.findByAttemptId(attemptId);

        long correctCount = answers.stream()
                .filter(a -> Boolean.TRUE.equals(a.getIsCorrect()))
                .count();
        double overallAccuracy = answers.isEmpty()
                ? 0.0
                : (correctCount * 100.0 / answers.size());

        GapAnalysis analysis = gapAnalysisRepository.findByAttemptId(attemptId)
                .orElseGet(GapAnalysis::new);
        analysis.setAttemptId(attemptId);
        analysis.setUserId(attempt.getUserId());
        analysis.setCertificateId(0L);
        analysis.setOverallAccuracy(overallAccuracy);
        analysis.setGeneratedAt(LocalDateTime.now());
        analysis.setAccuracyBySkillJson("{}");
        analysis.setAccuracyByTopicJson("{}");
        analysis.setAccuracyByDifficultyJson("{}");
        analysis.setWeakTopicsJson(overallAccuracy < THRESHOLD_DEVELOPING ? "[\"overall\"]" : "[]");
        analysis.setStrongTopicsJson(overallAccuracy > THRESHOLD_STRONG ? "[\"overall\"]" : "[]");

        GapAnalysis saved = gapAnalysisRepository.save(analysis);
        generateRecommendations(attempt.getUserId(), overallAccuracy, saved.getGapAnalysisId());

        log.info("Completed gap analysis for attempt {}, accuracy={}", attemptId, overallAccuracy);
        return toGapAnalysisResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public GapAnalysisResponse getAnalysisByAttempt(Long attemptId) {
        return gapAnalysisRepository.findByAttemptId(attemptId)
                .map(this::toGapAnalysisResponse)
                .orElseThrow(() -> new CommonException(ErrorCode.ENTITY_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public ProgressResponse getProgressOverview(Long userId, Long certificateId) {
        LearningProgress progress = progressRepository
                .findByUserIdAndCertificateIdOrderBySnapshotAtDesc(userId, certificateId)
                .stream()
                .findFirst()
                .orElse(null);

        return ProgressResponse.builder()
                .userId(userId)
                .certificateId(certificateId)
                .averageScore(progress != null ? progress.getAverageScore() : null)
                .totalAttempts(progress != null ? progress.getTotalAttempts() : 0)
                .masteryBySkillJson(progress != null ? progress.getMasteryBySkillJson() : null)
                .scoreTrendJson(progress != null ? progress.getScoreTrendJson() : null)
                .recentAttempts(new ArrayList<>())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecommendationResponse> getRecommendations(Long userId) {
        return recommendationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toRecommendationResponse)
                .toList();
    }

    @Override
    public RecommendationResponse startRecommendation(Long recommendationId) {
        return updateRecommendationStatus(recommendationId, RecommendationStatus.STARTED);
    }

    @Override
    public RecommendationResponse completeRecommendation(Long recommendationId) {
        return updateRecommendationStatus(recommendationId, RecommendationStatus.COMPLETED);
    }

    private void generateRecommendations(Long userId, double accuracy, Long gapAnalysisId) {
        if (accuracy < THRESHOLD_WEAK) {
            recommendationRepository.save(Recommendation.builder()
                    .userId(userId)
                    .gapAnalysisId(gapAnalysisId)
                    .priority(adaptivelearning.shared.enums.RecommendationPriority.HIGH)
                    .reason("Performance below 40% indicates critical gaps requiring immediate attention.")
                    .build());
        } else if (accuracy < THRESHOLD_DEVELOPING) {
            recommendationRepository.save(Recommendation.builder()
                    .userId(userId)
                    .gapAnalysisId(gapAnalysisId)
                    .priority(adaptivelearning.shared.enums.RecommendationPriority.HIGH)
                    .reason("Performance below 60% indicates weak areas that need focused practice.")
                    .build());
        } else if (accuracy < THRESHOLD_STRONG) {
            recommendationRepository.save(Recommendation.builder()
                    .userId(userId)
                    .gapAnalysisId(gapAnalysisId)
                    .priority(adaptivelearning.shared.enums.RecommendationPriority.MEDIUM)
                    .reason("Performance is developing. Continue practicing to strengthen weak topics.")
                    .build());
        }
    }

    private RecommendationResponse updateRecommendationStatus(Long id, RecommendationStatus status) {
        Recommendation recommendation = recommendationRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.ENTITY_NOT_FOUND));
        recommendation.setStatus(status);
        return toRecommendationResponse(recommendationRepository.save(recommendation));
    }

    private GapAnalysisResponse toGapAnalysisResponse(GapAnalysis a) {
        return GapAnalysisResponse.builder()
                .gapAnalysisId(a.getGapAnalysisId())
                .attemptId(a.getAttemptId())
                .userId(a.getUserId())
                .certificateId(a.getCertificateId())
                .accuracyBySkillJson(a.getAccuracyBySkillJson())
                .accuracyByTopicJson(a.getAccuracyByTopicJson())
                .accuracyByDifficultyJson(a.getAccuracyByDifficultyJson())
                .weakTopicsJson(a.getWeakTopicsJson())
                .strongTopicsJson(a.getStrongTopicsJson())
                .overallAccuracy(a.getOverallAccuracy())
                .generatedAt(a.getGeneratedAt())
                .build();
    }

    private RecommendationResponse toRecommendationResponse(Recommendation r) {
        return RecommendationResponse.builder()
                .recommendationId(r.getRecommendationId())
                .userId(r.getUserId())
                .gapAnalysisId(r.getGapAnalysisId())
                .practicePackageId(r.getPracticePackageId())
                .reason(r.getReason())
                .priority(r.getPriority())
                .status(r.getStatus())
                .build();
    }
}
