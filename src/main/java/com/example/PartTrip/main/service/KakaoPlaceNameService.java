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
    static final double MAX_DISTANCE_METERS = 700;

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
                || latitude == null || longitude == null || apiKey.isBlank()
                || !PlaceSearchService.inKorea(latitude, longitude)) {
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
            return pick(body, latitude, longitude);
        } catch (Exception e) {
            log.warn("카카오 장소 이름 검색 실패: {}", e.getClass().getSimpleName());
            return null;
        }
    }

    static String pick(JsonNode body, double latitude, double longitude) {
        if (body == null) {
            return null;
        }
        for (JsonNode doc : body.path("documents")) {
            String placeName = doc.path("place_name").asText("");
            if (!hasHangul(placeName) || !doc.path("x").isTextual() || !doc.path("y").isTextual()) {
                continue;
            }
            double distance = distanceMeters(latitude, longitude,
                    Double.parseDouble(doc.path("y").asText()), Double.parseDouble(doc.path("x").asText()));
            if (distance <= MAX_DISTANCE_METERS) {
                return placeName.strip();
            }
        }
        return null;
    }

    static double distanceMeters(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.pow(Math.sin(dLon / 2), 2);
        return 2 * 6_371_000 * Math.asin(Math.sqrt(a));
    }
}
