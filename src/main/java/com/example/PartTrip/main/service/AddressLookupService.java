package com.example.PartTrip.main.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

@Slf4j
@Service
public class AddressLookupService {

    private static final String URL = "https://dapi.kakao.com/v2/local/geo/coord2address.json";

    @Value("${kakao.rest-api-key:}")
    private String apiKey;

    private final RestClient restClient = RestClient.builder()
            .requestFactory(ClientHttpRequestFactoryBuilder.detect().build(
                    ClientHttpRequestFactorySettings.defaults()
                            .withConnectTimeout(Duration.ofSeconds(2))
                            .withReadTimeout(Duration.ofSeconds(5))))
            .build();

    public String lookup(double latitude, double longitude) {
        if (!PlaceSearchService.inKorea(latitude, longitude)) {
            throw new IllegalArgumentException("국내 좌표만 주소로 바꿀 수 있습니다.");
        }
        if (apiKey == null || apiKey.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "주소 변환을 쓸 수 없습니다.");
        }
        JsonNode body;
        try {
            body = restClient.get()
                    .uri(URL + "?x={x}&y={y}", longitude, latitude)
                    .header("Authorization", "KakaoAK " + apiKey.trim())
                    .retrieve()
                    .body(JsonNode.class);
        } catch (Exception e) {
            log.warn("주소 변환 실패: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "주소 변환이 잠시 안 됩니다.");
        }
        return parse(body);
    }

    static String parse(JsonNode body) {
        if (body == null) {
            return null;
        }
        for (JsonNode doc : body.path("documents")) {
            String road = doc.path("road_address").path("address_name").asText("");
            if (!road.isBlank()) {
                return road;
            }
            String lot = doc.path("address").path("address_name").asText("");
            if (!lot.isBlank()) {
                return lot;
            }
        }
        return null;
    }
}
