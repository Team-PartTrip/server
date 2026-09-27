package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.response.PlannerScheduleResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

/** ODsay 국내 대중교통 경로 검색 클라이언트. 키는 odsay.api-key 설정으로 받는다. */
@Component
public class OdsayTransitRouteClient {

    private final RestClient restClient;
    private final String apiKey;

    public OdsayTransitRouteClient(@Value("${odsay.api-key:}") String apiKey) {
        this.apiKey = apiKey == null ? "" : apiKey.trim();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(2_000);
        requestFactory.setReadTimeout(5_000);
        this.restClient = RestClient.builder()
                .baseUrl("https://api.odsay.com")
                .requestFactory(requestFactory)
                .build();
    }

    public boolean isConfigured() {
        return !apiKey.isBlank();
    }

    public SearchResult search(double fromLongitude, double fromLatitude,
            double toLongitude, double toLatitude, String fromName, String toName) {
        if (!isConfigured()) return new SearchResult("WAITING_FOR_API_KEY", null);

        try {
            JsonNode response = restClient.get()
                    .uri(uri -> uri.path("/v1/api/searchPubTransPathT")
                            .queryParam("apiKey", apiKey)
                            .queryParam("SX", fromLongitude)
                            .queryParam("SY", fromLatitude)
                            .queryParam("EX", toLongitude)
                            .queryParam("EY", toLatitude)
                            .queryParam("OPT", 0)
                            .queryParam("output", "json")
                            .build())
                    .retrieve()
                    .body(JsonNode.class);
            PlannerScheduleResponseDto.RouteLeg route = parse(response, fromName, toName);
            return route == null
                    ? new SearchResult("NO_ROUTE", null)
                    : new SearchResult("READY", route);
        } catch (RestClientException | IllegalArgumentException exception) {
            // API 키가 포함될 수 있는 예외 URL은 로그에 남기지 않는다.
            return new SearchResult("API_ERROR", null);
        }
    }

    static PlannerScheduleResponseDto.RouteLeg parse(
            JsonNode response, String fromName, String toName) {
        JsonNode paths = response == null ? null : response.path("result").path("path");
        if (paths == null || !paths.isArray() || paths.isEmpty()) return null;

        JsonNode bestPath = paths.get(0);
        JsonNode info = bestPath.path("info");
        if (!info.path("totalTime").canConvertToInt()) return null;

        List<PlannerScheduleResponseDto.RouteStep> steps = new ArrayList<>();
        int walkingMinutes = 0;
        for (JsonNode segment : bestPath.path("subPath")) {
            int trafficType = segment.path("trafficType").asInt(-1);
            int duration = segment.path("sectionTime").asInt(0);
            if (trafficType == 3) {
                walkingMinutes += duration;
                steps.add(new PlannerScheduleResponseDto.RouteStep(
                        "WALK", "도보", null, null, null, duration));
                continue;
            }
            if (trafficType != 1 && trafficType != 2) continue;

            JsonNode lane = segment.path("lane");
            if (lane.isArray()) lane = lane.path(0);
            String type = trafficType == 2 ? "BUS" : "SUBWAY";
            String name = trafficType == 2
                    ? firstText(lane, "busNo", "name", "nameKor")
                    : firstText(lane, "name", "nameKor");
            steps.add(new PlannerScheduleResponseDto.RouteStep(
                    type,
                    name,
                    firstText(segment, "startName", "startNameKor"),
                    firstText(segment, "endName", "endNameKor"),
                    segment.path("stationCount").canConvertToInt()
                            ? segment.path("stationCount").asInt() : null,
                    duration));
        }

        return new PlannerScheduleResponseDto.RouteLeg(
                "PUBLIC_TRANSIT", fromName, toName,
                info.path("totalTime").asInt(), walkingMinutes, List.copyOf(steps));
    }

    private static String firstText(JsonNode node, String... fields) {
        for (String field : fields) {
            String value = node.path(field).asText();
            if (!value.isBlank()) return value;
        }
        return null;
    }

    public record SearchResult(String status, PlannerScheduleResponseDto.RouteLeg route) {}
}
