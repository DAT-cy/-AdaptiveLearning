package adaptivelearning.module.ai.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
@Slf4j
public class OpenAiProvider implements AiProvider {

    private final WebClient webClient;
    private final String apiKey;

    public OpenAiProvider(WebClient.Builder webClientBuilder,
                          @Value("${ai.api-key:}") String apiKey,
                          @Value("${ai.base-url:https://api.openai.com/v1}") String baseUrl) {
        this.apiKey = apiKey;
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public Mono<String> chatCompletion(String systemPrompt, String userPrompt, String model, int maxTokens) {
        return executeRequest(systemPrompt, userPrompt, model, maxTokens);
    }

    @Override
    public String chatCompletionSync(String systemPrompt, String userPrompt, String model, int maxTokens) {
        return executeRequest(systemPrompt, userPrompt, model, maxTokens)
                .block(Duration.ofSeconds(120));
    }

    private Mono<String> executeRequest(String systemPrompt, String userPrompt, String model, int maxTokens) {
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                ),
                "max_tokens", maxTokens
        );

        return webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(ChatCompletionResponse.class)
                .map(response -> {
                    if (response.getChoices() == null || response.getChoices().isEmpty()) {
                        throw new RuntimeException("Empty response from AI provider");
                    }
                    return response.getChoices().get(0).getMessage().getContent();
                })
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .filter(this::isRetryable)
                        .onRetryExhaustedThrow((spec, signal) -> signal.failure()))
                .onErrorMap(ex -> {
                    log.error("AI provider call failed after retries: {}", ex.getMessage(), ex);
                    return new RuntimeException("AI provider error: " + ex.getMessage(), ex);
                });
    }

    private boolean isRetryable(Throwable throwable) {
        String message = throwable.getMessage();
        if (message == null) {
            return false;
        }
        return message.contains("429")
                || message.contains("500")
                || message.contains("502")
                || message.contains("503")
                || message.contains("timeout");
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.Builder
    public static class ChatCompletionResponse {
        private String id;
        private String object;
        private long created;
        private String model;
        private List<Choice> choices;
        private Usage usage;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.Builder
    public static class Choice {
        private int index;
        private Message message;
        @JsonProperty("finish_reason")
        private String finishReason;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.Builder
    public static class Message {
        private String role;
        private String content;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.Builder
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;
        @JsonProperty("completion_tokens")
        private int completionTokens;
        @JsonProperty("total_tokens")
        private int totalTokens;
    }
}
