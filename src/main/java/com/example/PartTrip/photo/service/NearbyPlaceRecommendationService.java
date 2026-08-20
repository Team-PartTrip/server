package com.example.PartTrip.photo.service;

import com.example.PartTrip.photo.dto.NearbyPlaceRecommendationResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class NearbyPlaceRecommendationService {

    private static final double SEARCH_RADIUS_METERS = 1500.0;
    private static final int MAX_RESULTS = 10;
    private static final List<String> INCLUDED_TYPES = List.of(
            "tourist_attraction", "museum", "park", "historical_landmark"
    );

    @Value("${google.places.api-key:}")
    private String googlePlacesApiKey;

    private final RestClient restClient = RestClient.create("https://places.googleapis.com");

    public List<NearbyPlaceRecommendationResponseDto> recommend(BigDecimal latitude, BigDecimal longitude) {
        if (googlePlacesApiKey == null || googlePlacesApiKey.isBlank()) {
            return List.of(placeholder(latitude, longitude));
        }

        Map<String, Object> requestBody = Map.of(
                "includedTypes", INCLUDED_TYPES,
                "maxResultCount", MAX_RESULTS,
                "rankPreference", "DISTANCE",
                "locationRestriction", Map.of(
                        "circle", Map.of(
                                "center", Map.of(
                                        "latitude", latitude.doubleValue(),
                                        "longitude", longitude.doubleValue()
                                ),
                                "radius", SEARCH_RADIUS_METERS
                        )
                )
        );

        Map<String, Object> response;
        try {
            response = restClient.post()
                    .uri("/v1/places:searchNearby")
                    .header("X-Goog-Api-Key", googlePlacesApiKey)
                    .header("X-Goog-FieldMask",
                            "places.displayName,places.formattedAddress,places.location,"
                                    + "places.editorialSummary,places.primaryTypeDisplayName,places.googleMapsUri")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);
        } catch (Exception e) {
            return List.of(placeholder(latitude, longitude));
        }

        if (response == null || !(response.get("places") instanceof List<?> rawPlaces) || rawPlaces.isEmpty()) {
            return List.of();
        }

        List<NearbyPlaceRecommendationResponseDto> results = new ArrayList<>();
        for (Object rawPlace : rawPlaces) {
            Map<String, Object> place = (Map<String, Object>) rawPlace;

            Map<String, Object> displayName = (Map<String, Object>) place.get("displayName");
            String name = displayName != null ? (String) displayName.get("text") : "이름 없음";

            Map<String, Object> location = (Map<String, Object>) place.get("location");
            double placeLat = location != null
                    ? ((Number) location.get("latitude")).doubleValue() : latitude.doubleValue();
            double placeLng = location != null
                    ? ((Number) location.get("longitude")).doubleValue() : longitude.doubleValue();

            int distance = (int) Math.round(
                    haversineMeters(latitude.doubleValue(), longitude.doubleValue(), placeLat, placeLng));

            results.add(NearbyPlaceRecommendationResponseDto.builder()
                    .name(name)
                    .description(extractDescription(place))
                    .latitude(BigDecimal.valueOf(placeLat))
                    .longitude(BigDecimal.valueOf(placeLng))
                    .distanceMeters(distance)
                    .sourceName("Google Places")
                    .sourceUrl((String) place.get("googleMapsUri"))
                    .build());
        }

        return results;
    }

    private String extractDescription(Map<String, Object> place) {
        Map<String, Object> editorialSummary = (Map<String, Object>) place.get("editorialSummary");
        if (editorialSummary != null && editorialSummary.get("text") != null) {
            return (String) editorialSummary.get("text");
        }
        Map<String, Object> primaryType = (Map<String, Object>) place.get("primaryTypeDisplayName");
        if (primaryType != null && primaryType.get("text") != null) {
            return (String) primaryType.get("text");
        }
        Object formattedAddress = place.get("formattedAddress");
        return formattedAddress != null ? (String) formattedAddress : "";
    }

    private double haversineMeters(double lat1, double lon1, double lat2, double lon2) {
        double earthRadiusMeters = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusMeters * c;
    }

    private NearbyPlaceRecommendationResponseDto placeholder(BigDecimal latitude, BigDecimal longitude) {
        return NearbyPlaceRecommendationResponseDto.builder()
                .name("주변 명소 추천 준비 중")
                .description("지도 API 키가 연결되면 현재 위치 기반 추천 결과로 교체됩니다.")
                .latitude(latitude)
                .longitude(longitude)
                .distanceMeters(0)
                .sourceName("PartTrip")
                .sourceUrl("https://example.com")
                .build();
    }
}
