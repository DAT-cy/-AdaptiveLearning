package adaptivelearning.module.adaptive.service;

import adaptivelearning.module.adaptive.dto.response.AdaptiveRecommendationResponse;
import adaptivelearning.module.adaptive.dto.response.LearnerProfileResponse;
import adaptivelearning.module.adaptive.dto.response.ScoreForecastResponse;
import java.util.List;

public interface AdaptiveService {

    LearnerProfileResponse getOrCreateProfile(Long userId, Long certificateId);

    LearnerProfileResponse updateProfile(Long userId, Long attemptId);

    List<AdaptiveRecommendationResponse> recommend(Long userId, Long certificateId);

    ScoreForecastResponse forecast(Long userId, Long certificateId);
}
