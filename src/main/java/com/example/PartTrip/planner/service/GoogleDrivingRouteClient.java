package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.response.PlannerScheduleResponseDto;
import com.example.PartTrip.profile.enums.PreferredTransport;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/** Google Routes API로 자동차·택시(주행) 또는 도보의 구간 소요 시간만 조회한다. */
@Component
public class GoogleDrivingRouteClient {

    private final RestClient restClient;
    private final String apiKey;

    /** API 키를 받아 Google Routes HTTP 클라이언트를 구성한다. */
    public GoogleDrivingRouteClient(@Value("${google.routes.api-key:}") String apiKey) {
        this.apiKey = apiKey == null ? "" : apiKey.trim();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(2_000);
        requestFactory.setReadTimeout(5_000);
        this.restClient = RestClient.builder()
                .baseUrl("https://routes.googleapis.com")
                .requestFactory(requestFactory)
                .build();
    }

    /** Google Routes API 키가 설정되어 있는지 반환한다. */
    public boolean isConfigured() {
        return !apiKey.isBlank();
    }

    /** 자동차·택시·도보 구간의 총 이동 시간을 조회한다. */
    public SearchResult search(PreferredTransport transport,
            double fromLatitude, double fromLongitude,
            double toLatitude, double toLongitude,
            String fromName, String toName) {
        if (!isConfigured()) return new SearchResult("WAITING_FOR_API_KEY", null);

        String travelMode = transport == PreferredTransport.WALKING ? "WALK" : "DRIVE";
        Map<String, Object> body = Map.of(
                "origin", location(fromLatitude, fromLongitude),
                "destination", location(toLatitude, toLongitude),
                "travelMode", travelMode,
                "routingPreference", "TRAFFIC_UNAWARE",
                "languageCode", "ko",
                "units", "METRIC"
        );

        try {
            JsonNode response = restClient.post()
                    .uri("/directions/v2:computeRoutes")
                    .header("X-Goog-Api-Key", apiKey)
                    .header("X-Goog-FieldMask", "routes.duration")
                    .body(body)
                    .retrieve()
                    .body(JsonNode.class);
            Integer minutes = parseDurationMinutes(response);
            if (minutes == null) return new SearchResult("NO_ROUTE", null);
            PlannerScheduleResponseDto.RouteLeg route = new PlannerScheduleResponseDto.RouteLeg(
                    transport.name(), fromName, toName, minutes,
                    transport == PreferredTransport.WALKING ? minutes : null, List.of());
            return new SearchResult("READY", route);
        } catch (RestClientException | IllegalArgumentException exception) {
            // 키나 요청 URL 등 민감한 값이 포함될 수 있는 예외는 기록하지 않는다.
            return new SearchResult("API_ERROR", null);
        }
    }

    /** Google Routes의 초 단위 소요 시간을 올림해 분으로 변환한다. */
    static Integer parseDurationMinutes(JsonNode response) {
        JsonNode routes = response == null ? null : response.path("routes");
        if (routes == null || !routes.isArray() || routes.isEmpty()) return null;
        String duration = routes.path(0).path("duration").asText();
        if (!duration.endsWith("s")) return null;
        try {
            BigDecimal seconds = new BigDecimal(duration.substring(0, duration.length() - 1));
            if (seconds.signum() < 0) return null;
            return seconds.divide(BigDecimal.valueOf(60), 0, RoundingMode.CEILING).intValueExact();
        } catch (ArithmeticException | NumberFormatException exception) {
            return null;
        }
    }

    /** Google Routes 요청 형식의 좌표 객체를 만든다. */
    private Map<String, Object> location(double latitude, double longitude) {
        return Map.of("location", Map.of("latLng", Map.of(
                "latitude", latitude,
                "longitude", longitude)));
    }

    public record SearchResult(String status, PlannerScheduleResponseDto.RouteLeg route) {}
}
