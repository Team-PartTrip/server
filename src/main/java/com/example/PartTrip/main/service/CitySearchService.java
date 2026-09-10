package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.CitySearchResponseDto;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.util.CountryCodeMapper;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CitySearchService {

    private static final String ADMIN_SUFFIXES = "시구군정촌현도";

    private final TourPlaceRepository tourPlaceRepository;

    private static final String AUTOCOMPLETE_URL =
            "https://places.googleapis.com/v1/places:autocomplete";

    // 필요한 것만 받는다. 도시 이름과 나라 이름만 쓴다.
    private static final String FIELD_MASK =
            "suggestions.placePrediction.structuredFormat";

    /** 자동완성은 입력 한 글자마다 부를 수 있어 넉넉히 기다리지 않는다 */
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(3);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(5);

    /** 한 글자로 부르면 나라 전체가 걸려 요금만 나간다 */
    private static final int MIN_KEYWORD_LENGTH = 2;

    /** 구글이 한 번에 5개까지만 준다. 그보다 크게 잡아도 의미가 없다 */
    private static final int MAX_RESULTS = 5;

    @Value("${google.places.api-key}")
    private String apiKey;

    private final RestClient restClient = RestClient.builder()
            .requestFactory(ClientHttpRequestFactoryBuilder.detect().build(
                    ClientHttpRequestFactorySettings.defaults()
                            .withConnectTimeout(CONNECT_TIMEOUT)
                            .withReadTimeout(READ_TIMEOUT)))
            .build();

    /** @param countryName null 이면 전 세계에서 찾는다 */
    public List<CitySearchResponseDto> search(String countryName, String keyword) {
        String trimmed = keyword == null ? "" : keyword.trim();
        if (trimmed.length() < MIN_KEYWORD_LENGTH) {
            return List.of();
        }
        try {
            return withKnownNames(parse(request(countryName, trimmed), countryName));
        } catch (Exception e) {
            // 검색이 안 되는 것과 앱이 죽는 것은 다르다. 비어서 돌려준다.
            log.warn("도시 검색 실패 ({} / {}): {}", countryName, trimmed, e.getMessage());
            return List.of();
        }
    }

    private JsonNode request(String countryName, String keyword) {
        Map<String, Object> body = new HashMap<>();
        body.put("input", keyword);
        // 도시만 받는다. 안 걸면 식당·거리까지 섞여 나온다.
        body.put("includedPrimaryTypes", List.of("(cities)"));
        body.put("languageCode", "ko");

        String code = countryName == null ? null
                : CountryCodeMapper.getCountryCode(countryName);
        if (code != null) {
            // 나라를 안 줬거나 코드를 못 찾으면 필터 없이 전 세계에서 찾는다.
            // 어설프게 막느니 넓게 보여주는 편이 낫다.
            body.put("includedRegionCodes", List.of(code));
        }

        return restClient.post()
                .uri(AUTOCOMPLETE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Goog-Api-Key", apiKey)
                .header("X-Goog-FieldMask", FIELD_MASK)
                .body(body)
                .retrieve()
                .body(JsonNode.class);
    }

    List<CitySearchResponseDto> withKnownNames(List<CitySearchResponseDto> cities) {
        List<CitySearchResponseDto> fixed = new ArrayList<>(cities.size());
        Map<String, List<String>> knownByCountry = new HashMap<>();

        for (CitySearchResponseDto city : cities) {
            List<String> known = knownByCountry.computeIfAbsent(
                    city.getCountryName(),
                    country -> country == null || country.isBlank()
                            ? List.of()
                            : tourPlaceRepository.findCityNames(country));
            String matched = knownName(city.getCityName(), known);
            fixed.add(matched == null
                    ? city
                    : new CitySearchResponseDto(matched, city.getCountryName()));
        }
        return fixed;
    }

    private String knownName(String cityName, List<String> known) {
        for (String name : known) {
            if (name.equals(cityName)) {
                return null;    // 이미 같은 이름이라 바꿀 것이 없다
            }
            if (cityName.length() == name.length() + 1
                    && cityName.startsWith(name)
                    && ADMIN_SUFFIXES.indexOf(cityName.charAt(name.length())) >= 0) {
                return name;
            }
        }
        return null;
    }

    /** 같은 도시가 두 번 오는 일이 있어 이름으로 한 번 거른다 */
    List<CitySearchResponseDto> parse(JsonNode body, String countryName) {
        if (body == null) {
            return List.of();
        }
        Set<String> seen = new LinkedHashSet<>();
        List<CitySearchResponseDto> cities = new ArrayList<>();

        for (JsonNode suggestion : body.path("suggestions")) {
            JsonNode format = suggestion.path("placePrediction").path("structuredFormat");
            String city = format.path("mainText").path("text").asText("");
            if (city.isBlank() || !seen.add(city)) {
                continue;
            }
            // 한국어로 받으면 secondaryText 가 "일본 오사카부" 처럼 나라부터 온다.
            // 콤마도 없다. 요청한 나라를 먼저 쓰고, 없을 때만 첫 조각을 쓴다.
            String secondary = format.path("secondaryText").path("text").asText("");
            cities.add(new CitySearchResponseDto(city, country(secondary, countryName)));

            if (cities.size() == MAX_RESULTS) {
                break;
            }
        }
        return cities;
    }

    private String country(String secondary, String fallback) {
        if (fallback != null && !fallback.isBlank()) {
            return fallback;
        }
        if (secondary == null || secondary.isBlank()) {
            // 나라 없이 검색했는데 secondaryText 도 없으면 알 길이 없다
            return "";
        }
        return secondary.trim().split("[\\s,]+")[0];
    }
}
