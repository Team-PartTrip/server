package com.example.PartTrip.main.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 구글 플레이스로 tour_place 를 다시 채운다.
 *
 * 사용자 요청마다 부르지 않고 여기서 한 번에 받아 저장한다. Places 는
 * 요청당 과금이라, 화면이 열릴 때마다 부르면 사용자 수에 비례해 돈이 나간다.
 * 앱·웹이 쓰는 GET /api/main/tour-place 는 그대로 두고 데이터만 갈아끼운다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TourPlaceImportService {

    private static final String SEARCH_URL =
            "https://places.googleapis.com/v1/places:searchText";

    // 필요한 것만 받는다. Places 는 요청한 필드에 따라 요금이 달라진다.
    private static final String FIELD_MASK = String.join(",",
            "places.id",
            "places.displayName",
            "places.formattedAddress",
            "places.rating",
            "places.location",
            "places.editorialSummary",
            "places.photos");

    /** 카테고리마다 한글로 검색한다. 한국어 결과가 그대로 화면에 들어간다 */
    private static final Map<TourPlaceCategory, String> KEYWORD = Map.of(
            TourPlaceCategory.RESTAURANT, "맛집",
            TourPlaceCategory.ATTRACTION, "관광 명소",
            TourPlaceCategory.ACCOMMODATION, "호텔",
            TourPlaceCategory.CAFE, "카페",
            TourPlaceCategory.ACTIVITY, "액티비티 체험",
            TourPlaceCategory.SHOPPING, "쇼핑");

    /** 카테고리당 가져올 개수. 요청 수가 아니라 한 요청의 결과 수다 */
    private static final int PER_CATEGORY = 10;

    private final TourPlaceRepository tourPlaceRepository;

    @Value("${google.places.api-key}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();

    /**
     * 도시들을 다시 채운다.
     *
     * @param cities "일본/오사카" 형식. 나라와 도시를 / 로 붙여 넘긴다
     * @return 도시별로 저장한 개수
     */
    public Map<String, Integer> importCities(List<String> cities) {
        Map<String, Integer> saved = new LinkedHashMap<>();

        for (String entry : cities) {
            String[] parts = entry.split("/", 2);
            if (parts.length != 2) {
                throw new IllegalArgumentException(
                        "도시는 \"나라/도시\" 형식이어야 합니다: " + entry);
            }
            String countryName = parts[0].trim();
            String cityName = parts[1].trim();

            List<TourPlaceEntity> places = fetchCity(countryName, cityName);
            if (places.isEmpty()) {
                // 하나도 못 받았는데 지우면 그 도시가 통째로 비어버린다
                log.warn("{} {} — 받아온 장소가 없어 건너뜁니다", countryName, cityName);
                saved.put(entry, 0);
                continue;
            }

            backup(cityName);
            tourPlaceRepository.deleteByCityName(cityName);
            tourPlaceRepository.saveAll(places);
            saved.put(entry, places.size());
            log.info("{} {} — {}개 저장", countryName, cityName, places.size());
        }
        return saved;
    }

    /** 한 도시를 카테고리별로 받아 온다. 이름이 같으면 먼저 온 것만 남긴다 */
    private List<TourPlaceEntity> fetchCity(String countryName, String cityName) {
        Map<String, TourPlaceEntity> byName = new LinkedHashMap<>();

        for (TourPlaceCategory category : TourPlaceCategory.values()) {
            String query = cityName + " " + KEYWORD.get(category);
            JsonNode body;
            try {
                body = search(query);
            } catch (Exception e) {
                // 한 카테고리가 실패해도 나머지는 채운다
                log.warn("{} 검색 실패: {}", query, e.getMessage());
                continue;
            }

            for (JsonNode place : body.path("places")) {
                String name = place.path("displayName").path("text").asText(null);
                if (name == null || name.isBlank() || byName.containsKey(name)) {
                    continue;
                }
                byName.put(name, toEntity(place, countryName, cityName, category));
            }
        }
        return new ArrayList<>(byName.values());
    }

    private JsonNode search(String textQuery) {
        return restClient.post()
                .uri(SEARCH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Goog-Api-Key", apiKey)
                .header("X-Goog-FieldMask", FIELD_MASK)
                .body(Map.of(
                        "textQuery", textQuery,
                        "languageCode", "ko",
                        "maxResultCount", PER_CATEGORY))
                .retrieve()
                .body(JsonNode.class);
    }

    private TourPlaceEntity toEntity(
            JsonNode place,
            String countryName,
            String cityName,
            TourPlaceCategory category
    ) {
        TourPlaceEntity entity = new TourPlaceEntity();
        entity.setCountryName(countryName);
        entity.setCityName(cityName);
        entity.setPlaceName(place.path("displayName").path("text").asText());
        entity.setCategory(category);
        entity.setAddress(text(place, "formattedAddress", 500));
        entity.setDescription(
                place.path("editorialSummary").path("text").isMissingNode()
                        ? null
                        : cut(place.path("editorialSummary").path("text").asText(), 1000));
        entity.setRating(place.path("rating").isNumber()
                ? place.path("rating").asDouble()
                : null);
        entity.setLatitude(place.path("location").path("latitude").isNumber()
                ? place.path("location").path("latitude").asDouble()
                : null);
        entity.setLongitude(place.path("location").path("longitude").isNumber()
                ? place.path("location").path("longitude").asDouble()
                : null);
        entity.setImageUrl(photoUrl(place));
        return entity;
    }

    /**
     * 사진 주소를 가져온다.
     *
     * media 주소에 키를 붙여 그대로 저장하면 API 키가 앱까지 나간다.
     * skipHttpRedirect=true 로 부르면 키가 없는 최종 주소를 JSON 으로 준다.
     */
    private String photoUrl(JsonNode place) {
        JsonNode photos = place.path("photos");
        if (!photos.isArray() || photos.isEmpty()) {
            return null;
        }
        String photoName = photos.get(0).path("name").asText(null);
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
            return cut(body.path("photoUri").asText(null), 1000);
        } catch (Exception e) {
            // 사진이 없어도 목록은 그려진다. 앱이 imageUrl null 을 이미 처리한다
            log.warn("사진 주소 실패 ({}): {}", photoName, e.getMessage());
            return null;
        }
    }

    /** 지우기 전에 되돌릴 수 있게 남긴다 */
    private void backup(String cityName) {
        List<TourPlaceEntity> old = tourPlaceRepository.findByCityName(cityName);
        if (old.isEmpty()) {
            return;
        }
        String stamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path file = Path.of("tour_place_backup_" + cityName + "_" + stamp + ".sql");

        StringBuilder sql = new StringBuilder();
        for (TourPlaceEntity p : old) {
            sql.append("INSERT INTO tour_place ")
                    .append("(country_name, city_name, place_name, category, description, ")
                    .append("address, rating, image_url, latitude, longitude) VALUES (")
                    .append(quote(p.getCountryName())).append(", ")
                    .append(quote(p.getCityName())).append(", ")
                    .append(quote(p.getPlaceName())).append(", ")
                    .append(quote(p.getCategory() == null ? null : p.getCategory().name())).append(", ")
                    .append(quote(p.getDescription())).append(", ")
                    .append(quote(p.getAddress())).append(", ")
                    .append(p.getRating()).append(", ")
                    .append(quote(p.getImageUrl())).append(", ")
                    .append(p.getLatitude()).append(", ")
                    .append(p.getLongitude()).append(");\n");
        }
        try {
            Files.writeString(file, sql.toString(), StandardCharsets.UTF_8);
            log.info("백업 {}줄 → {}", old.size(), file.toAbsolutePath());
        } catch (IOException e) {
            // 백업을 못 남기면 지우면 안 된다
            throw new IllegalStateException("백업 파일을 쓰지 못했습니다: " + file, e);
        }
    }

    private static String quote(String value) {
        return value == null ? "NULL" : "'" + value.replace("'", "''") + "'";
    }

    private static String text(JsonNode node, String field, int max) {
        String value = node.path(field).asText(null);
        return cut(value, max);
    }

    private static String cut(String value, int max) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.length() <= max ? value : value.substring(0, max);
    }
}
