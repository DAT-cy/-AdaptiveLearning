package adaptivelearning.module.analytics.controller;

import adaptivelearning.config.response.DefaultRes;
import adaptivelearning.module.analytics.dto.response.*;
import adaptivelearning.module.analytics.service.AnalyticsService;
import adaptivelearning.utils.ClientUtils;
import adaptivelearning.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping(ClientUtils.VERSION + "/analytics") @RequiredArgsConstructor
@Tag(name = "Analytics", description = "Gap analysis, progress tracking, recommendations")
public class AnalyticsController {
    private final AnalyticsService service;
    @PostMapping("/attempts/{attemptId}/analyze") public ResponseEntity<DefaultRes<GapAnalysisResponse>> analyze(@PathVariable Long attemptId) { return ResponseEntity.ok(DefaultRes.res(200, "Success", service.analyzeAttempt(attemptId))); }
    @GetMapping("/attempts/{attemptId}/analysis") public ResponseEntity<DefaultRes<GapAnalysisResponse>> analysis(@PathVariable Long attemptId) { return ResponseEntity.ok(DefaultRes.res(200, "Success", service.getAnalysisByAttempt(attemptId))); }
    @GetMapping("/me/progress") public ResponseEntity<DefaultRes<ProgressResponse>> progress(@RequestParam Long certificateId) { return ResponseEntity.ok(DefaultRes.res(200, "Success", service.getProgressOverview(SecurityUtils.getCurrentUserId(), certificateId))); }
    @GetMapping("/me/recommendations") public ResponseEntity<DefaultRes<List<RecommendationResponse>>> recommendations() { return ResponseEntity.ok(DefaultRes.res(200, "Success", service.getRecommendations(SecurityUtils.getCurrentUserId()))); }
    @PostMapping("/recommendations/{id}/start") public ResponseEntity<DefaultRes<RecommendationResponse>> start(@PathVariable Long id) { return ResponseEntity.ok(DefaultRes.res(200, "Started", service.startRecommendation(id))); }
    @PostMapping("/recommendations/{id}/complete") public ResponseEntity<DefaultRes<RecommendationResponse>> complete(@PathVariable Long id) { return ResponseEntity.ok(DefaultRes.res(200, "Completed", service.completeRecommendation(id))); }
}
