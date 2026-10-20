package com.example.PartTrip.planner.service;

import com.example.PartTrip.global.exception.AiUnavailableException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/** OpenAI Chat Completions 를 JSON 모드로 부른다 */
@Slf4j
@Component
public class OpenAiClient {

    private static final String URL = "https://api.openai.com/v1/chat/completions";
    private static final String FAILED = "AI가 일정을 만들지 못했어요. 잠시 후 다시 시도해주세요.";

    @Value("${openai.api-key:}")
    private String apiKey;

    // 모델을 바꿀 때 코드를 고치지 않도록 설정으로 뺀다
    @Value("${openai.model:gpt-4.1-mini}")
    private String model;

    // 후보 수십 개를 보고 며칠치를 짜면 수십 초가 걸린다
    private final RestClient restClient = RestClient.builder()
            .requestFactory(ClientHttpRequestFactoryBuilder.detect().build(
                    ClientHttpRequestFactorySettings.defaults()
                            .withConnectTimeout(Duration.ofSeconds(5))
                            .withReadTimeout(Duration.ofSeconds(90))))
            .build();

    /** 답의 content(JSON 문자열)를 돌려준다 */
    public String completeJson(String system, String user) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new AiUnavailableException("AI 키가 설정되지 않았습니다. openai.api-key 를 넣어주세요.");
        }
        try {
            JsonNode body = restClient.post()
                    .uri(URL)
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "model", model,
                            "temperature", 0.3,
                            "response_format", Map.of("type", "json_object"),
                            "messages", List.of(
                                    Map.of("role", "system", "content", system),
                                    Map.of("role", "user", "content", user))))
                    .retrieve()
                    .body(JsonNode.class);
            String content = body == null ? null
                    : body.path("choices").path(0).path("message").path("content").asText(null);
            if (content == null || content.isBlank()) {
                throw new AiUnavailableException(FAILED);
            }
            return content;
        } catch (RestClientException e) {
            // 응답 본문에 키가 섞여 나오지 않는다. 메시지만 남긴다
            log.warn("OpenAI 호출 실패: {}", e.getMessage());
            throw new AiUnavailableException(FAILED);
        }
    }
}
