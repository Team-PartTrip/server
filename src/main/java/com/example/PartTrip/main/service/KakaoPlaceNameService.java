package com.example.PartTrip.main.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class KakaoPlaceNameService {

    private static final int RADIUS_METERS = 500;

    private final RestClient restClient;
    private final String apiKey;

    public KakaoPlaceNameService(@Value("${kakao.rest-api-key:}") String apiKey) {
        this.apiKey = apiKey == null ? "" : apiKey.trim();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(2_000);
        requestFactory.setReadTimeout(5_000);
        this.restClient = RestClient.builder()
                .baseUrl("https://dapi.kakao.com")
                .requestFactory(requestFactory)
                .build();
    }

    public static boolean hasHangul(String name) {
        return name != null && name.codePoints().anyMatch(c ->
                (c >= 0xAC00 && c <= 0xD7A3) || (c >= 0x3131 && c <= 0x318E));
    }

    public String koreanName(String name, Double latitude, Double longitude) {
        if (hasHangul(name) || name == null || name.isBlank()
                || latitude == null || longitude == null || apiKey.isBlank()) {
            return null;
        }
        try {
            JsonNode body = restClient.get()
                    .uri(uri -> uri.path("/v2/local/search/keyword.json")
                            .queryParam("query", name)
                            .queryParam("x", longitude)
                            .queryParam("y", latitude)
                            .queryParam("radius", RADIUS_METERS)
                            .queryParam("sort", "distance")
                            .queryParam("size", 5)
                            .build())
                    .header("Authorization", "KakaoAK " + apiKey)
                    .retrieve()
                    .body(JsonNode.class);
            return pick(body);
        } catch (Exception e) {
            log.warn("카카오 장소 이름 검색 실패: {}", e.getClass().getSimpleName());
            return null;
        }
    }

    static String pick(JsonNode body) {
        if (body == null) {
            return null;
        }
        for (JsonNode doc : body.path("documents")) {
            String placeName = doc.path("place_name").asText("");
            if (hasHangul(placeName)) {
                return placeName.strip();
            }
        }
        return null;
    }
}
