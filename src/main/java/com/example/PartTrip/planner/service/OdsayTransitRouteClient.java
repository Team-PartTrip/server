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
import java.util.Map;

/** ODsay 국내 대중교통 경로 검색 클라이언트. 키는 odsay.api-key 설정으로 받는다. */
@Component
public class OdsayTransitRouteClient {

    static final double WALK_ONLY_METERS = 700;
    static final double INTERCITY_METERS = 30_000;
    private static final double WALK_DETOUR = 1.3;
    private static final double WALK_METERS_PER_MINUTE = 67;

    private static final Map<Integer, String> INTERCITY_TYPES = Map.of(
            4, "TRAIN", 5, "EXPRESS_BUS", 6, "INTERCITY_BUS", 7, "AIR");

    private final RestClient restClient;
    private final String apiKey;

    /** API 키를 받아 타임아웃이 제한된 ODsay HTTP 클라이언트를 구성한다. */
    public OdsayTransitRouteClient(
            @Value("${ODSAY_API_KEY:${odsay.api-key:}}") String apiKey) {
        this.apiKey = apiKey == null ? "" : apiKey.trim();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(2_000);
        requestFactory.setReadTimeout(5_000);
        this.restClient = RestClient.builder()
                .baseUrl("https://api.odsay.com")
                .requestFactory(requestFactory)
                .build();
    }

    /** ODsay API 키가 설정되어 있는지 반환한다. */
    public boolean isConfigured() {
        return !apiKey.isBlank();
    }

    /** 두 좌표 사이의 대중교통 경로를 조회하고 API 실패를 상태값으로 변환한다. */
    public SearchResult search(double fromLongitude, double fromLatitude,
            double toLongitude, double toLatitude, String fromName, String toName) {
        if (!isConfigured()) return new SearchResult("WAITING_FOR_API_KEY", null);
        double meters = distanceMeters(fromLatitude, fromLongitude, toLatitude, toLongitude);
        if (meters <= WALK_ONLY_METERS) {
            return new SearchResult("READY", walking(meters, fromName, toName));
        }

        try {
            JsonNode path = pickPath(request(fromLongitude, fromLatitude, toLongitude, toLatitude),
                    meters >= INTERCITY_METERS);
            PlannerScheduleResponseDto.RouteLeg route = toLeg(path, fromName, toName);
            if (route != null && hasIntercitySegment(path)) {
                route = withLocalLegs(route, path, fromLongitude, fromLatitude,
                        toLongitude, toLatitude, fromName, toName);
            }
            return route == null
                    ? new SearchResult("NO_ROUTE", null)
                    : new SearchResult("READY", route);
        } catch (RestClientException | IllegalArgumentException exception) {
            // API 키가 포함될 수 있는 예외 URL은 로그에 남기지 않는다.
            return new SearchResult("API_ERROR", null);
        }
    }

    /** ODsay 응답에서 최적 경로의 교통수단, 정류장, 시간을 추출한다. */
    static PlannerScheduleResponseDto.RouteLeg parse(
            JsonNode response, String fromName, String toName) {
        return parse(response, fromName, toName, false);
    }

    static PlannerScheduleResponseDto.RouteLeg parse(
            JsonNode response, String fromName, String toName, boolean preferIntercity) {
        return toLeg(pickPath(response, preferIntercity), fromName, toName);
    }

    private JsonNode request(double fromLongitude, double fromLatitude,
            double toLongitude, double toLatitude) {
        return restClient.get()
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
    }

    static JsonNode pickPath(JsonNode response, boolean preferIntercity) {
        JsonNode paths = response == null ? null : response.path("result").path("path");
        if (paths == null || !paths.isArray() || paths.isEmpty()) return null;
        if (preferIntercity) {
            for (JsonNode path : paths) {
                if (hasIntercitySegment(path)) return path;
            }
        }
        return paths.get(0);
    }

    private PlannerScheduleResponseDto.RouteLeg withLocalLegs(
            PlannerScheduleResponseDto.RouteLeg intercity, JsonNode path,
            double fromLongitude, double fromLatitude, double toLongitude, double toLatitude,
            String fromName, String toName) {
        JsonNode first = null;
        JsonNode last = null;
        for (JsonNode segment : path.path("subPath")) {
            if (!INTERCITY_TYPES.containsKey(segment.path("trafficType").asInt(-1))) continue;
            if (first == null) first = segment;
            last = segment;
        }
        PlannerScheduleResponseDto.RouteLeg head = local(fromLongitude, fromLatitude,
                first.path("startX").asDouble(Double.NaN), first.path("startY").asDouble(Double.NaN),
                fromName, firstText(first, "startName", "startNameKor"));
        PlannerScheduleResponseDto.RouteLeg tail = local(
                last.path("endX").asDouble(Double.NaN), last.path("endY").asDouble(Double.NaN),
                toLongitude, toLatitude, firstText(last, "endName", "endNameKor"), toName);
        return join(intercity, head, tail);
    }

    private PlannerScheduleResponseDto.RouteLeg local(double fromLongitude, double fromLatitude,
            double toLongitude, double toLatitude, String fromName, String toName) {
        if (Double.isNaN(fromLongitude) || Double.isNaN(fromLatitude)
                || Double.isNaN(toLongitude) || Double.isNaN(toLatitude)) return null;
        double meters = distanceMeters(fromLatitude, fromLongitude, toLatitude, toLongitude);
        if (meters <= WALK_ONLY_METERS) return walking(meters, fromName, toName);
        try {
            return toLeg(pickPath(request(fromLongitude, fromLatitude, toLongitude, toLatitude), false),
                    fromName, toName);
        } catch (RestClientException | IllegalArgumentException exception) {
            return null;
        }
    }

    static PlannerScheduleResponseDto.RouteLeg join(PlannerScheduleResponseDto.RouteLeg intercity,
            PlannerScheduleResponseDto.RouteLeg head, PlannerScheduleResponseDto.RouteLeg tail) {
        List<PlannerScheduleResponseDto.RouteStep> steps = new ArrayList<>();
        int total = 0;
        int walking = 0;
        for (PlannerScheduleResponseDto.RouteLeg leg : new PlannerScheduleResponseDto.RouteLeg[]{head, intercity, tail}) {
            if (leg == null) continue;
            steps.addAll(leg.steps());
            total += leg.durationMinutes() == null ? 0 : leg.durationMinutes();
            walking += leg.walkingMinutes() == null ? 0 : leg.walkingMinutes();
        }
        return new PlannerScheduleResponseDto.RouteLeg("PUBLIC_TRANSIT",
                intercity.fromName(), intercity.toName(), total, walking, List.copyOf(steps));
    }

    static PlannerScheduleResponseDto.RouteLeg toLeg(JsonNode bestPath, String fromName, String toName) {
        if (bestPath == null) return null;
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
            String intercity = INTERCITY_TYPES.get(trafficType);
            if (intercity != null) {
                steps.add(new PlannerScheduleResponseDto.RouteStep(
                        intercity,
                        null,
                        firstText(segment, "startName", "startNameKor"),
                        firstText(segment, "endName", "endNameKor"),
                        null,
                        duration));
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

    private static boolean hasIntercitySegment(JsonNode path) {
        for (JsonNode segment : path.path("subPath")) {
            if (INTERCITY_TYPES.containsKey(segment.path("trafficType").asInt(-1))) return true;
        }
        return false;
    }

    static PlannerScheduleResponseDto.RouteLeg walking(double meters, String fromName, String toName) {
        int minutes = Math.max(1, (int) Math.ceil(meters * WALK_DETOUR / WALK_METERS_PER_MINUTE));
        return new PlannerScheduleResponseDto.RouteLeg(
                "WALKING", fromName, toName, minutes, minutes,
                List.of(new PlannerScheduleResponseDto.RouteStep(
                        "WALK", "도보", null, null, null, minutes)));
    }

    static double distanceMeters(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.pow(Math.sin(dLon / 2), 2);
        return 2 * 6_371_000 * Math.asin(Math.sqrt(a));
    }

    /** 여러 버전의 ODsay 응답 필드명 중 처음으로 채워진 문자열을 선택한다. */
    private static String firstText(JsonNode node, String... fields) {
        for (String field : fields) {
            String value = node.path(field).asText();
            if (!value.isBlank()) return value;
        }
        return null;
    }

    public record SearchResult(String status, PlannerScheduleResponseDto.RouteLeg route) {}
}
