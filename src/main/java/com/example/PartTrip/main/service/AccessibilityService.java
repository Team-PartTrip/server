package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.AccessibilityResponseDto;
import com.example.PartTrip.main.entity.TourPlaceAccessibilityEntity;
import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.repository.TourPlaceAccessibilityRepository;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessibilityService {

    private static final String BASE = "https://apis.data.go.kr/B551011/KorWithService2";
    private static final int RADIUS_METERS = 500;
    static final double NAME_MATCH_METERS = 500;
    static final double NEAR_METERS = 30;
    static final Duration FRESH = Duration.ofDays(30);

    static final List<String[]> FIELDS = List.of(
            new String[]{"ELEVATOR", "elevator", "엘리베이터"},
            new String[]{"RESTROOM", "restroom", "화장실"},
            new String[]{"ROUTE", "route", "접근로"},
            new String[]{"EXIT", "exit", "출입구"},
            new String[]{"WHEELCHAIR", "wheelchair", "휠체어"},
            new String[]{"PARKING", "parking", "주차"},
            new String[]{"PUBLIC_TRANSPORT", "publictransport", "대중교통"});

    private final TourPlaceRepository tourPlaceRepository;
    private final TourPlaceAccessibilityRepository accessibilityRepository;
    private final ObjectMapper objectMapper;

    @Value("${tour-api.service-key:}")
    private String serviceKey;

    private final RestClient restClient = RestClient.builder()
            .requestFactory(ClientHttpRequestFactoryBuilder.detect().build(
                    ClientHttpRequestFactorySettings.defaults()
                            .withConnectTimeout(Duration.ofSeconds(3))
                            .withReadTimeout(Duration.ofSeconds(5))))
            .build();

    public AccessibilityResponseDto get(Long tourPlaceId) {
        TourPlaceEntity place = tourPlaceRepository.findById(tourPlaceId)
                .orElseThrow(() -> new IllegalArgumentException("장소를 찾을 수 없습니다."));

        Optional<TourPlaceAccessibilityEntity> saved = accessibilityRepository.findById(tourPlaceId);
        if (saved.isPresent() && saved.get().getFetchedAt().isAfter(LocalDateTime.now().minus(FRESH))) {
            return toDto(saved.get());
        }
        if (serviceKey == null || serviceKey.isBlank()
                || place.getLatitude() == null || place.getLongitude() == null) {
            return AccessibilityResponseDto.none();
        }

        TourPlaceAccessibilityEntity row;
        try {
            row = fetch(place);
        } catch (Exception e) {
            log.warn("무장애 정보 받기 실패 tourPlaceId={}: {}", tourPlaceId, e.getMessage());
            return saved.map(this::toDto).orElse(AccessibilityResponseDto.none());
        }
        accessibilityRepository.save(row);
        return toDto(row);
    }

    private TourPlaceAccessibilityEntity fetch(TourPlaceEntity place) throws Exception {
        TourPlaceAccessibilityEntity row = new TourPlaceAccessibilityEntity();
        row.setTourPlaceId(place.getTourPlaceId());
        row.setFetchedAt(LocalDateTime.now());

        List<JsonNode> candidates = new ArrayList<>();
        String keyword = place.getPlaceName() == null ? "" : place.getPlaceName().replaceAll("\\(.*?\\)", "").strip();
        if (!keyword.isEmpty()) {
            for (JsonNode item : itemsOf(call("searchKeyword2", "&numOfRows=20&keyword="
                    + URLEncoder.encode(keyword, StandardCharsets.UTF_8)))) {
                double meters = meters(place.getLatitude(), place.getLongitude(),
                        item.path("mapy").asDouble(), item.path("mapx").asDouble());
                candidates.add(((ObjectNode) item.deepCopy())
                        .put("dist", String.valueOf(meters)));
            }
        }
        candidates.addAll(itemsOf(call("locationBasedList2", "&mapX=" + place.getLongitude()
                + "&mapY=" + place.getLatitude() + "&radius=" + RADIUS_METERS + "&arrange=E&numOfRows=20")));
        Optional<JsonNode> match = pick(place.getPlaceName(), candidates);
        if (match.isEmpty()) {
            row.setItemsJson("[]");
            return row;
        }
        String contentId = match.get().path("contentid").asText();
        row.setContentId(contentId);
        row.setMatchedTitle(match.get().path("title").asText(null));

        List<JsonNode> detail = itemsOf(call("detailWithTour2", "&contentId=" + contentId));
        List<AccessibilityResponseDto.Item> items = detail.isEmpty() ? List.of() : items(detail.get(0));
        row.setItemsJson(objectMapper.writeValueAsString(items));
        return row;
    }

    private JsonNode call(String operation, String params) throws Exception {
        String url = BASE + "/" + operation
                + "?serviceKey=" + URLEncoder.encode(serviceKey, StandardCharsets.UTF_8)
                + "&MobileOS=ETC&MobileApp=Dandi&_type=json" + params;
        JsonNode body = objectMapper.readTree(restClient.get().uri(URI.create(url)).retrieve().body(String.class));
        String error = body.path("OpenAPI_ServiceResponse").path("cmmMsgHeader").path("errMsg").asText("");
        if (!error.isEmpty()) {
            throw new IllegalStateException(error);
        }
        return body;
    }

    static Optional<JsonNode> pick(String placeName, List<JsonNode> candidates) {
        String name = normalize(placeName);
        Comparator<JsonNode> nearest = Comparator.comparingDouble(AccessibilityService::dist);
        Optional<JsonNode> byName = candidates.stream()
                .filter(c -> dist(c) <= NAME_MATCH_METERS && similar(name, normalize(c.path("title").asText(""))))
                .min(nearest);
        if (byName.isPresent()) {
            return byName;
        }
        return candidates.stream().filter(c -> dist(c) <= NEAR_METERS).min(nearest);
    }

    static List<AccessibilityResponseDto.Item> items(JsonNode detail) {
        List<AccessibilityResponseDto.Item> items = new ArrayList<>();
        for (String[] f : FIELDS) {
            String text = detail.path(f[1]).asText("").strip();
            if (!text.isEmpty()) {
                items.add(new AccessibilityResponseDto.Item(f[0], f[2], text));
            }
        }
        return items;
    }

    static String normalize(String name) {
        if (name == null) {
            return "";
        }
        return name.replaceAll("\\(.*?\\)|\\[.*?]", "")
                .replaceAll("[^\\p{L}\\p{N}]", "")
                .toLowerCase();
    }

    static double meters(double lat1, double lng1, double lat2, double lng2) {
        double r = 6_371_000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        return 2 * r * Math.asin(Math.sqrt(a));
    }

    private static boolean similar(String a, String b) {
        return a.length() >= 2 && b.length() >= 2 && (a.contains(b) || b.contains(a));
    }

    private static double dist(JsonNode candidate) {
        return candidate.path("dist").asDouble(Double.MAX_VALUE);
    }

    static List<JsonNode> itemsOf(JsonNode body) {
        JsonNode item = body.path("response").path("body").path("items").path("item");
        List<JsonNode> nodes = new ArrayList<>();
        if (item.isArray()) {
            item.forEach(nodes::add);
        } else if (item.isObject()) {
            nodes.add(item);
        }
        return nodes;
    }

    private AccessibilityResponseDto toDto(TourPlaceAccessibilityEntity row) {
        if (row.getContentId() == null) {
            return AccessibilityResponseDto.none();
        }
        try {
            List<AccessibilityResponseDto.Item> items = objectMapper.readValue(
                    row.getItemsJson(), new TypeReference<>() {});
            return new AccessibilityResponseDto(true, items);
        } catch (Exception e) {
            return AccessibilityResponseDto.none();
        }
    }
}
