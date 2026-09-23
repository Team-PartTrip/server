package com.example.PartTrip.main.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourPlacePhotoService {

    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(3);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(5);
    /** 한 번에 이만큼씩 저장한다. 다 받고 한 번에 저장하면 중간에 죽을 때 전부 날아간다 */
    private static final int SAVE_EVERY = 10;

    private final TourPlaceRepository tourPlaceRepository;

    @Value("${google.places.api-key}")
    private String apiKey;

    private final RestClient restClient = RestClient.builder()
            .requestFactory(ClientHttpRequestFactoryBuilder.detect().build(
                    ClientHttpRequestFactorySettings.defaults()
                            .withConnectTimeout(CONNECT_TIMEOUT)
                            .withReadTimeout(READ_TIMEOUT)))
            .build();

    /** 검색 응답에서 첫 사진의 이름을 꺼낸다. 없으면 null */
    public static String photoNameOf(JsonNode place) {
        JsonNode photos = place.path("photos");
        if (!photos.isArray() || photos.isEmpty()) {
            return null;
        }
        String name = photos.get(0).path("name").asText(null);
        return name == null || name.isBlank() ? null : name;
    }

    @Async
    public void fillAsync(List<TourPlaceEntity> places, Map<String, String> photoNames) {
        if (photoNames.isEmpty()) {
            return;
        }
        List<TourPlaceEntity> filled = new ArrayList<>();
        int done = 0;
        for (TourPlaceEntity place : places) {
            // 이미 사진이 있으면 다시 받지 않는다
            if (place.getImageUrl() != null) {
                continue;
            }
            String url = resolve(photoNames.get(place.getPlaceName()));
            if (url == null) {
                continue;
            }
            place.setImageUrl(url);
            filled.add(place);
            if (filled.size() >= SAVE_EVERY) {
                tourPlaceRepository.saveAll(filled);
                done += filled.size();
                filled.clear();
            }
        }
        if (!filled.isEmpty()) {
            tourPlaceRepository.saveAll(filled);
            done += filled.size();
        }
        log.info("사진 {}개 채움", done);
    }

    public String resolve(String photoName) {
        if (photoName == null) {
            return null;
        }
        try {
            // URI 템플릿을 쓰면 photoName 안의 / 가 %2F 로 바뀌어 경로가 깨진다.
            // 이미 안전한 문자만 들어 있으므로 그대로 이어 붙인다.
            JsonNode body = restClient.get()
                    .uri(URI.create("https://places.googleapis.com/v1/" + photoName
                            + "/media?maxHeightPx=800&skipHttpRedirect=true&key=" + apiKey))
                    .retrieve()
                    .body(JsonNode.class);
            String url = body == null ? null : body.path("photoUri").asText(null);
            return url == null || url.length() <= 1000 ? url : url.substring(0, 1000);
        } catch (Exception e) {
            // 사진이 없어도 목록은 그려진다. 앱이 imageUrl null 을 이미 처리한다
            log.warn("사진 주소 실패 ({}): {}", photoName, e.getMessage());
            return null;
        }
    }
}
