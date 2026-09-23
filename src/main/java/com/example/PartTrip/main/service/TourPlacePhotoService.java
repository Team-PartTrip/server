package com.example.PartTrip.main.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourPlacePhotoService {

    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(3);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(5);
    /** 구글 주소를 기억하는 시간. 구글이 유효 기간을 밝히지 않아서 짧게 잡는다 */
    static final Duration REMEMBER = Duration.ofHours(1);

    private final TourPlaceRepository tourPlaceRepository;

    @Value("${google.places.api-key}")
    private String apiKey;

    private final RestClient restClient = RestClient.builder()
            .requestFactory(ClientHttpRequestFactoryBuilder.detect().build(
                    ClientHttpRequestFactorySettings.defaults()
                            .withConnectTimeout(CONNECT_TIMEOUT)
                            .withReadTimeout(READ_TIMEOUT)))
            .build();

    private record Remembered(String uri, Instant until) {}

    private final Map<Long, Remembered> remembered = new ConcurrentHashMap<>();

    public static String photoPath(Long tourPlaceId) {
        return "/api/main/tour-place/" + tourPlaceId + "/photo";
    }

    /** 검색 응답에서 첫 사진의 이름을 꺼낸다. 없으면 null */
    public static String photoNameOf(JsonNode place) {
        JsonNode photos = place.path("photos");
        if (!photos.isArray() || photos.isEmpty()) {
            return null;
        }
        String name = photos.get(0).path("name").asText(null);
        return name == null || name.isBlank() ? null : name;
    }

    public List<TourPlaceEntity> attachPhotos(List<TourPlaceEntity> saved) {
        List<TourPlaceEntity> changed = saved.stream()
                .filter(place -> place.getPhotoName() != null && place.getTourPlaceId() != null)
                .peek(place -> place.setImageUrl(photoPath(place.getTourPlaceId())))
                .toList();
        return changed.isEmpty() ? changed : tourPlaceRepository.saveAll(changed);
    }

    public Optional<String> currentUri(Long tourPlaceId) {
        Instant now = Instant.now();
        Remembered hit = remembered.get(tourPlaceId);
        if (hit != null && hit.until().isAfter(now)) {
            return Optional.of(hit.uri());
        }
        String photoName = tourPlaceRepository.findById(tourPlaceId)
                .map(TourPlaceEntity::getPhotoName)
                .orElse(null);
        if (photoName == null) {
            return Optional.empty();
        }
        String uri = resolve(photoName);
        if (uri == null) {
            return Optional.empty();
        }
        remembered.put(tourPlaceId, new Remembered(uri, now.plus(REMEMBER)));
        return Optional.of(uri);
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
            return body == null ? null : body.path("photoUri").asText(null);
        } catch (Exception e) {
            // 사진이 없어도 목록은 그려진다
            log.warn("사진 주소 실패 ({}): {}", photoName, e.getMessage());
            return null;
        }
    }
}
