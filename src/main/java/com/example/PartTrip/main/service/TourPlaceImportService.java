package com.example.PartTrip.main.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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

    static final Map<TourPlaceCategory, List<String>> MORE_KEYWORDS = Map.of(
            TourPlaceCategory.RESTAURANT, List.of("맛집", "음식점", "현지 맛집", "레스토랑", "식당"),
            TourPlaceCategory.ATTRACTION, List.of("관광 명소", "가볼만한 곳", "랜드마크", "박물관", "공원"),
            TourPlaceCategory.ACCOMMODATION, List.of("호텔", "숙소", "게스트하우스", "리조트"),
            TourPlaceCategory.CAFE, List.of("카페", "디저트 카페", "베이커리", "찻집"),
            TourPlaceCategory.ACTIVITY, List.of("액티비티 체험", "투어", "테마파크", "체험"),
            TourPlaceCategory.SHOPPING, List.of("쇼핑", "쇼핑몰", "시장", "기념품 가게"));

    private static final int PAGE_SIZE = 20;
    private static final int ENOUGH = 10;
    private static final int MAX_CALLS_PER_REQUEST = 3;

    private static final String PAGE_FIELD_MASK = String.join(",",
            "places.displayName",
            "places.formattedAddress",
            "places.rating",
            "places.location",
            "places.photos",
            "nextPageToken");
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(30);

    private final TourPlaceRepository tourPlaceRepository;
    private final TransactionTemplate transactionTemplate;
    private final TourPlacePhotoService tourPlacePhotoService;

    @Value("${google.places.api-key}")
    private String apiKey;

    private final Map<String, Object> moreLocks = new ConcurrentHashMap<>();

    private final RestClient restClient = RestClient.builder()
            .requestFactory(ClientHttpRequestFactoryBuilder.detect().build(
                    ClientHttpRequestFactorySettings.defaults()
                            .withConnectTimeout(CONNECT_TIMEOUT)
                            .withReadTimeout(READ_TIMEOUT)))
            .build();

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

            // 파일 백업은 트랜잭션 밖에서 먼저 끝낸다
            backup(countryName, cityName);
            // 지우기와 넣기를 한 트랜잭션으로 묶는다. 따로 두면 삭제가 먼저
            // 커밋되고 저장이 실패했을 때 그 도시가 통째로 비어버린다.
            transactionTemplate.executeWithoutResult(status -> {
                tourPlaceRepository.deleteByCountryNameAndCityName(countryName, cityName);
                tourPlacePhotoService.attachPhotos(tourPlaceRepository.saveAll(places));
            });
            saved.put(entry, places.size());
            log.info("{} {} — {}개 저장", countryName, cityName, places.size());
        }
        return saved;
    }

    public int fillMissingPhotos(String countryName, String cityName) {
        List<TourPlaceEntity> saved = tourPlaceRepository
                .findByCountryNameAndCityName(countryName, cityName).stream()
                .filter(place -> place.getPhotoName() == null)
                .toList();
        if (saved.isEmpty()) {
            return 0;
        }
        Map<String, String> photoNames = new LinkedHashMap<>();
        fetchCity(countryName, cityName).forEach(place -> {
            if (place.getPhotoName() != null) {
                photoNames.putIfAbsent(place.getPlaceName(), place.getPhotoName());
            }
        });
        // 더 보기로 들어온 장소는 도시 기본 검색에 안 나온다. 그 검색어로 한 번 더 찾는다
        if (saved.stream().anyMatch(place -> !photoNames.containsKey(place.getPlaceName()))) {
            morePhotoNames(cityName).forEach(photoNames::putIfAbsent);
        }
        List<TourPlaceEntity> named = new ArrayList<>();
        for (TourPlaceEntity place : saved) {
            String photoName = photoNames.get(place.getPlaceName());
            if (photoName != null) {
                place.setPhotoName(photoName);
                named.add(place);
            }
        }
        tourPlacePhotoService.attachPhotos(named);
        log.info("{} {} — 사진 이름 없던 {}곳 중 {}곳 채움",
                countryName, cityName, saved.size(), named.size());
        return named.size();
    }

    /** 더 보기에 쓰는 검색어로 장소 이름 → 사진 이름을 모은다. 저장하지는 않는다 */
    private Map<String, String> morePhotoNames(String cityName) {
        Map<String, String> names = new LinkedHashMap<>();
        MORE_KEYWORDS.values().stream().flatMap(List::stream).distinct().forEach(keyword -> {
            try {
                JsonNode body = searchPage(cityName + " " + keyword, null);
                for (JsonNode place : body.path("places")) {
                    String name = place.path("displayName").path("text").asText(null);
                    String photoName = TourPlacePhotoService.photoNameOf(place);
                    if (name != null && photoName != null) {
                        names.putIfAbsent(name, photoName);
                    }
                }
            } catch (Exception e) {
                log.warn("{} {} 사진 이름 검색 실패: {}", cityName, keyword, e.getMessage());
            }
        });
        return names;
    }

    /**
     * 아직 한 번도 안 받아온 도시면 지금 받아온다.
     *
     * 여행지 검색이 나라 전체로 넓어지면서, 관광지 데이터가 없는 도시도
     * 고를 수 있게 됐다. 그대로 두면 장소 목록이 비어 다음으로 못 넘어간다.
     *
     * 이미 있는 도시는 건드리지 않는다. 사진은 이름만 저장하고, 주소는 앱이
     * 사진을 열 때 받는다(#170). 여기서 구글 사진 요청은 나가지 않는다.
     *
     * @return 새로 채웠으면 true
     */
    public boolean importCityIfEmpty(String countryName, String cityName) {
        if (countryName == null || cityName == null || cityName.isBlank()) {
            return false;
        }
        if (!tourPlaceRepository.findByCountryNameAndCityName(countryName, cityName).isEmpty()) {
            return false;
        }
        List<TourPlaceEntity> places = fetchCity(countryName, cityName);
        if (places.isEmpty()) {
            log.info("{} {} — 받아올 장소가 없다", countryName, cityName);
            return false;
        }
        List<TourPlaceEntity> saved = tourPlaceRepository.saveAll(places);
        tourPlacePhotoService.attachPhotos(saved);
        log.info("{} {} — 처음 열려서 {}개 받아옴", countryName, cityName, saved.size());
        return true;
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

    /** 더 받은 결과. cursor 가 null 이면 구글이 더 줄 게 없다 */
    public record MoreResult(List<TourPlaceEntity> places, String cursor) {}

    record Cursor(int keyword, String pageToken) {

        static Cursor decode(String value) {
            if (value == null || value.isBlank()) {
                return new Cursor(0, null);
            }
            try {
                String raw = new String(Base64.getUrlDecoder().decode(value), StandardCharsets.UTF_8);
                int bar = raw.indexOf('|');
                int keyword = Integer.parseInt(raw.substring(0, bar));
                String token = raw.substring(bar + 1);
                return new Cursor(keyword, token.isEmpty() ? null : token);
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("cursor 가 올바르지 않습니다.");
            }
        }

        String encode() {
            String raw = keyword + "|" + (pageToken == null ? "" : pageToken);
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(raw.getBytes(StandardCharsets.UTF_8));
        }
    }

    /** 다음 쪽이 있으면 그 쪽, 없으면 다음 검색어, 검색어도 끝났으면 null */
    static Cursor next(Cursor current, String nextPageToken, int keywordCount) {
        if (nextPageToken != null && !nextPageToken.isBlank()) {
            return new Cursor(current.keyword(), nextPageToken);
        }
        if (current.keyword() + 1 < keywordCount) {
            return new Cursor(current.keyword() + 1, null);
        }
        return null;
    }

    public MoreResult fetchMore(
            String countryName, String cityName, TourPlaceCategory category, String cursorValue) {
        List<String> keywords = MORE_KEYWORDS.get(category);
        Cursor cursor = Cursor.decode(cursorValue);
        if (cursor.keyword() < 0 || cursor.keyword() >= keywords.size()) {
            throw new IllegalArgumentException("cursor 가 올바르지 않습니다.");
        }

        // 같은 도시·카테고리를 두 사람이 동시에 내리면 둘 다 같은 장소를
        // 새것으로 보고 저장한다. 도시·카테고리마다 한 줄로 세운다.
        String lockKey = countryName + "|" + cityName + "|" + category;
        synchronized (moreLocks.computeIfAbsent(lockKey, key -> new Object())) {
            Set<String> known = new HashSet<>();
            tourPlaceRepository.findByCountryNameAndCityName(countryName, cityName)
                    .forEach(place -> known.add(place.getPlaceName()));

            List<TourPlaceEntity> fresh = new ArrayList<>();
            int calls = 0;
            while (cursor != null && fresh.size() < ENOUGH && calls < MAX_CALLS_PER_REQUEST) {
                String query = cityName + " " + keywords.get(cursor.keyword());
                JsonNode body;
                try {
                    body = searchPage(query, cursor.pageToken());
                } catch (Exception e) {
                    log.warn("{} 더 받기 실패: {}", query, e.getMessage());
                    break;
                }
                calls++;

                for (JsonNode place : body.path("places")) {
                    String name = place.path("displayName").path("text").asText(null);
                    if (name == null || name.isBlank() || !known.add(name)) {
                        continue;
                    }
                    fresh.add(toEntity(place, countryName, cityName, category));
                }
                cursor = next(cursor, body.path("nextPageToken").asText(null), keywords.size());
            }

            List<TourPlaceEntity> saved = fresh.isEmpty() ? List.of() : tourPlaceRepository.saveAll(fresh);
            tourPlacePhotoService.attachPhotos(saved);
            log.info("{} {} {} — {}곳 더 받음 (구글 {}번)", countryName, cityName, category, saved.size(), calls);
            return new MoreResult(saved, cursor == null ? null : cursor.encode());
        }
    }

    private JsonNode searchPage(String textQuery, String pageToken) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("textQuery", textQuery);
        body.put("languageCode", "ko");
        body.put("pageSize", PAGE_SIZE);
        if (pageToken != null) {
            body.put("pageToken", pageToken);
        }
        return restClient.post()
                .uri(SEARCH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Goog-Api-Key", apiKey)
                .header("X-Goog-FieldMask", PAGE_FIELD_MASK)
                .body(body)
                .retrieve()
                .body(JsonNode.class);
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
        entity.setAddress(cleanAddress(text(place, "formattedAddress", 500), countryName));
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
        entity.setPhotoName(TourPlacePhotoService.photoNameOf(place));
        entity.setLongitude(place.path("location").path("longitude").isNumber()
                ? place.path("location").path("longitude").asDouble()
                : null);
        return entity;
    }



    /**
     * 구글이 주는 주소를 화면에 넣을 만큼만 남긴다.
     *
     * formattedAddress 는 나라 이름과 우편번호가 앞이나 뒤에 붙어 오는데
     * (형식이 나라마다 다르다), 목록에서는 한 줄로 잘려서 정작 필요한
     * 도시·구가 안 보인다.
     *
     *   "일본 〒542-0076 Osaka, Chuo Ward, Namba, 1-chōme−6−８"
     *     → "Osaka, Chuo Ward, Namba, 1-chōme−6−８"
     *   "323 Đ. Trần Hưng Đạo, An Hải, Đà Nẵng 550000 베트남"
     *     → "323 Đ. Trần Hưng Đạo, An Hải, Đà Nẵng"
     */
    static String cleanAddress(String address, String countryName) {
        if (address == null || address.isBlank()) {
            return null;
        }
        String cleaned = address
                // 일본 우편번호 (〒123-4567)
                .replaceAll("〒\\s*\\d{3}-\\d{4}", " ")
                // 그 나라 이름. 앞뒤 어디에 붙어도 뗀다.
                // 예전에는 목록을 박아뒀는데, 도시를 새로 받아올 때마다
                // 목록에 없는 나라(프랑스…)가 주소 끝에 그대로 남았다.
                .replaceAll(countryPattern(countryName), " ")
                // 남은 우편번호 (123-4567 · 550000 · 059919)
                .replaceAll("(^|[,\\s])\\d{3}-\\d{4}($|[,\\s])", " ")
                .replaceAll("(^|[,\\s])\\d{5,6}($|[,\\s])", " ")
                // 국가 코드가 끝에 남는 경우 (… 2층 KR)
                .replaceAll("[,\\s]+[A-Z]{2}$", "")
                // 위에서 지운 자리에 남은 빈 칸·쉼표 정리
                .replaceAll("\\s*,\\s*,\\s*", ", ")
                .replaceAll("\\s{2,}", " ")
                .trim()
                .replaceAll("^[,\\s]+", "")
                .replaceAll("[,\\s]+$", "");
        return cleaned.isBlank() ? null : cleaned;
    }

    /** 그 나라 이름과, 구글이 섞어 쓰는 영문 표기까지 함께 지운다 */
    private static String countryPattern(String countryName) {
        String korean = countryName == null || countryName.isBlank()
                ? ""
                : java.util.regex.Pattern.quote(countryName.trim());
        String english = englishNameOf(countryName);
        String names = korean.isEmpty() ? english
                : english.isEmpty() ? korean
                : korean + "|" + english;
        if (names.isEmpty()) {
            return "(?!)";  // 지울 것이 없으면 아무것도 안 맞게 둔다
        }
        return "(^|[,\\s])(" + names + ")($|[,\\s])";
    }

    /** "일본" → "Japan". ISO 목록에서 찾는다 */
    private static String englishNameOf(String countryName) {
        if (countryName == null || countryName.isBlank()) {
            return "";
        }
        String target = countryName.trim();
        for (String code : java.util.Locale.getISOCountries()) {
            java.util.Locale locale = java.util.Locale.of("", code);
            if (target.equals(locale.getDisplayCountry(java.util.Locale.KOREAN))) {
                return java.util.regex.Pattern.quote(
                        locale.getDisplayCountry(java.util.Locale.ENGLISH));
            }
        }
        return "";
    }

    /** 지우기 전에 되돌릴 수 있게 남긴다 */
    private void backup(String countryName, String cityName) {
        List<TourPlaceEntity> old = tourPlaceRepository
                .findByCountryNameAndCityName(countryName, cityName);
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
