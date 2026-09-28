package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.PlaceSearchResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PlaceSearchService {

    private static final String SEARCH_URL = "https://places.googleapis.com/v1/places:searchText";
    private static final String FIELD_MASK = "places.displayName,places.formattedAddress,places.location";
    static final int MAX_RESULTS = 5;

    @Value("${google.places.api-key:}")
    private String apiKey;

    private final RestClient restClient = RestClient.builder()
            .requestFactory(ClientHttpRequestFactoryBuilder.detect().build(
                    ClientHttpRequestFactorySettings.defaults()
                            .withConnectTimeout(Duration.ofSeconds(3))
                            .withReadTimeout(Duration.ofSeconds(5))))
            .build();

    public List<PlaceSearchResponseDto> search(String query) {
        String q = query == null ? "" : query.strip();
        if (q.isEmpty()) {
            return List.of();
        }
        if (q.length() > 50) {
            throw new IllegalArgumentException("검색어는 50자까지 쓸 수 있습니다.");
        }
        if (apiKey == null || apiKey.isBlank()) {
            return List.of();
        }
        try {
            JsonNode body = restClient.post()
                    .uri(SEARCH_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-Goog-Api-Key", apiKey)
                    .header("X-Goog-FieldMask", FIELD_MASK)
                    // 국내 여행 앱이라 한국 안에서만 찾는다
                    .body(Map.of(
                            "textQuery", q,
                            "languageCode", "ko",
                            "regionCode", "KR",
                            "maxResultCount", MAX_RESULTS))
                    .retrieve()
                    .body(JsonNode.class);
            return parse(body);
        } catch (Exception e) {
            log.warn("장소 검색 실패: {}", e.getMessage());
            return List.of();
        }
    }

    static List<PlaceSearchResponseDto> parse(JsonNode body) {
        List<PlaceSearchResponseDto> result = new ArrayList<>();
        if (body == null) {
            return result;
        }
        for (JsonNode place : body.path("places")) {
            JsonNode location = place.path("location");
            String name = place.path("displayName").path("text").asText("");
            if (name.isBlank() || !location.path("latitude").isNumber() || !location.path("longitude").isNumber()) {
                continue;
            }
            result.add(new PlaceSearchResponseDto(
                    name,
                    place.path("formattedAddress").asText(""),
                    location.path("latitude").asDouble(),
                    location.path("longitude").asDouble()));
        }
        return result;
    }
}
