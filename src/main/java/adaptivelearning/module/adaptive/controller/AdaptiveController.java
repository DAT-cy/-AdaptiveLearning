package adaptivelearning.module.adaptive.controller;

import adaptivelearning.config.response.DefaultRes;
import adaptivelearning.module.adaptive.dto.response.AdaptiveRecommendationResponse;
import adaptivelearning.module.adaptive.dto.response.LearnerProfileResponse;
import adaptivelearning.module.adaptive.dto.response.ScoreForecastResponse;
import adaptivelearning.module.adaptive.service.AdaptiveService;
import adaptivelearning.utils.ClientUtils;
import adaptivelearning.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ClientUtils.VERSION + "/adaptive")
@RequiredArgsConstructor
@Tag(name = "Adaptive Engine", description = "Learner profiles, adaptive recommendations, and score forecasts")
@PreAuthorize("isAuthenticated()")
public class AdaptiveController {

    private final AdaptiveService service;

    @GetMapping("/profile")
    @Operation(summary = "Get or create learner profile for a certificate")
    public ResponseEntity<DefaultRes<LearnerProfileResponse>> getProfile(
            @RequestParam Long certificateId) {
        Long userId = SecurityUtils.getCurrentUserId();
        LearnerProfileResponse data = service.getOrCreateProfile(userId, certificateId);
        return ResponseEntity.ok(DefaultRes.res(200, "Success", data));
    }

    @PostMapping("/attempts/{attemptId}/update")
    @Operation(summary = "Update learner profile based on attempt results")
    public ResponseEntity<DefaultRes<LearnerProfileResponse>> updateProfile(
            @PathVariable Long attemptId) {
        Long userId = SecurityUtils.getCurrentUserId();
        LearnerProfileResponse data = service.updateProfile(userId, attemptId);
        return ResponseEntity.ok(DefaultRes.res(200, "Updated", data));
    }

    @GetMapping("/recommend")
    @Operation(summary = "Get adaptive recommendations for weakest skills")
    public ResponseEntity<DefaultRes<List<AdaptiveRecommendationResponse>>> recommend(
            @RequestParam Long certificateId) {
        Long userId = SecurityUtils.getCurrentUserId();
        List<AdaptiveRecommendationResponse> data = service.recommend(userId, certificateId);
        return ResponseEntity.ok(DefaultRes.res(200, "Success", data));
    }

    @GetMapping("/forecast")
    @Operation(summary = "Get score forecast for a certificate")
    public ResponseEntity<DefaultRes<ScoreForecastResponse>> forecast(
            @RequestParam Long certificateId) {
        Long userId = SecurityUtils.getCurrentUserId();
        ScoreForecastResponse data = service.forecast(userId, certificateId);
        return ResponseEntity.ok(DefaultRes.res(200, "Success", data));
    }
}
