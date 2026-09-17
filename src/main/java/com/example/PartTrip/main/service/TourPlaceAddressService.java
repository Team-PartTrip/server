package com.example.PartTrip.main.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourPlaceAddressService {

    private static final String GEOCODE_URL =
            "https://maps.googleapis.com/maps/api/geocode/json";

    private final TourPlaceRepository tourPlaceRepository;

    @Value("${google.geocoding.api-key:${google.places.api-key}}")
    private String apiKey;

    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(10);

    private final RestClient restClient = RestClient.builder()
            .requestFactory(ClientHttpRequestFactoryBuilder.detect().build(
                    ClientHttpRequestFactorySettings.defaults()
                            .withConnectTimeout(CONNECT_TIMEOUT)
                            .withReadTimeout(READ_TIMEOUT)))
            .build();

    @Transactional
    public int translateAddresses() {
        List<TourPlaceEntity> all = tourPlaceRepository.findAll();
        int changed = 0;

        for (TourPlaceEntity place : all) {
            if (place.getLatitude() == null || place.getLongitude() == null) {
                continue;
            }
            if (hasKorean(place.getAddress())) {
                continue;
            }
            String korean = lookupKorean(place.getLatitude(), place.getLongitude());
            if (korean == null) {
                continue;
            }
            String cleaned = TourPlaceImportService.cleanAddress(
                    korean, place.getCountryName());
            if (cleaned == null || cleaned.equals(place.getAddress())) {
                continue;
            }
            if (changed < 5) {
                log.info("{} : {} → {}", place.getPlaceName(), place.getAddress(), cleaned);
            }
            place.setAddress(cleaned);
            changed++;
        }
        return changed;
    }

    private boolean hasKorean(String value) {
        return value != null
                && value.chars().anyMatch(c -> c >= 0xAC00 && c <= 0xD7A3);
    }

    private String lookupKorean(double latitude, double longitude) {
        try {
            JsonNode body = restClient.get()
                    .uri(URI.create(GEOCODE_URL
                            + "?latlng=" + latitude + "," + longitude
                            + "&language=ko&key=" + apiKey))
                    .retrieve()
                    .body(JsonNode.class);

            String status = body.path("status").asText();
            if (!"OK".equals(status)) {
                log.warn("역지오코딩 실패: {} {}", status,
                        body.path("error_message").asText(""));
                return null;
            }
            for (JsonNode result : body.path("results")) {
                String address = result.path("formatted_address").asText(null);
                if (hasKorean(address)) {
                    return address;
                }
            }
            return null;
        } catch (Exception e) {
            log.warn("역지오코딩 오류: {}", e.getMessage());
            return null;
        }
    }
}
