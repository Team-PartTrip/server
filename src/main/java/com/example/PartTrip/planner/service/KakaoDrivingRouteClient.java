package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.response.PlannerScheduleResponseDto;
import com.example.PartTrip.profile.enums.PreferredTransport;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class KakaoDrivingRouteClient {

    private final RestClient restClient;
    private final String apiKey;

    public KakaoDrivingRouteClient(@Value("${kakao.rest-api-key:}") String apiKey) {
        this.apiKey = apiKey == null ? "" : apiKey.trim();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(2_000);
        requestFactory.setReadTimeout(5_000);
        this.restClient = RestClient.builder()
                .baseUrl("https://apis-navi.kakaomobility.com")
                .requestFactory(requestFactory)
                .build();
    }

    public boolean isConfigured(PreferredTransport transport) {
        return transport == PreferredTransport.WALKING || !apiKey.isBlank();
    }

    public SearchResult search(PreferredTransport transport,
            double fromLatitude, double fromLongitude,
            double toLatitude, double toLongitude,
            String fromName, String toName) {
        if (transport == PreferredTransport.WALKING) {
            double meters = OdsayTransitRouteClient.distanceMeters(
                    fromLatitude, fromLongitude, toLatitude, toLongitude);
            return new SearchResult("READY", OdsayTransitRouteClient.walking(meters, fromName, toName));
        }
        if (!isConfigured(transport)) return new SearchResult("WAITING_FOR_API_KEY", null);

        try {
            JsonNode response = restClient.get()
                    .uri(uri -> uri.path("/v1/directions")
                            .queryParam("origin", fromLongitude + "," + fromLatitude)
                            .queryParam("destination", toLongitude + "," + toLatitude)
                            .queryParam("summary", true)
                            .build())
                    .header("Authorization", "KakaoAK " + apiKey)
                    .retrieve()
                    .body(JsonNode.class);
            Integer minutes = parseDurationMinutes(response);
            if (minutes == null) return new SearchResult("NO_ROUTE", null);
            return new SearchResult("READY", new PlannerScheduleResponseDto.RouteLeg(
                    transport.name(), fromName, toName, minutes, null, List.of()));
        } catch (RestClientException | IllegalArgumentException exception) {
            return new SearchResult("API_ERROR", null);
        }
    }

    static Integer parseDurationMinutes(JsonNode response) {
        JsonNode route = response == null ? null : response.path("routes").path(0);
        if (route == null || route.path("result_code").asInt(-1) != 0) return null;
        JsonNode seconds = route.path("summary").path("duration");
        if (!seconds.canConvertToInt() || seconds.asInt() < 0) return null;
        return Math.max(1, (int) Math.ceil(seconds.asInt() / 60.0));
    }

    public record SearchResult(String status, PlannerScheduleResponseDto.RouteLeg route) {}
}
