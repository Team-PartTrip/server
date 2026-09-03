package com.example.PartTrip.main.service;

import com.example.PartTrip.main.entity.CountryInfoEntity;
import com.example.PartTrip.main.repository.CountryInfoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * country_info 의 도시 이름을 한글로 바꾼다.
 *
 * 수도가 영문(Paris · Tokyo)으로 들어 있어서 여행지 검색에 "파리" 를 쳐도
 * 안 잡혔다. 화면에도 영문 그대로 나왔다.
 *
 * 구글 플레이스에 한국어로 물어 받은 이름으로 덮는다. 한 번 돌리면 되는
 * 작업이라 관광지 임포터와 같은 방식으로 속성을 준 실행에서만 돈다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CountryCityNameService {

    private static final String SEARCH_URL =
            "https://places.googleapis.com/v1/places:searchText";

    private final CountryInfoRepository countryInfoRepository;

    @Value("${google.places.api-key}")
    private String apiKey;

    /**
     * 타임아웃 없는 기본 클라이언트를 쓰면 상대가 응답을 안 줄 때 무한정
     * 기다린다. 이 작업은 트랜잭션 안에서 장소마다 도는 동기 호출이라,
     * 한 번 멈추면 커넥션을 쥔 채로 서버가 함께 묶인다.
     */
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(10);

    private final RestClient restClient = RestClient.builder()
            .requestFactory(ClientHttpRequestFactoryBuilder.detect().build(
                    ClientHttpRequestFactorySettings.defaults()
                            .withConnectTimeout(CONNECT_TIMEOUT)
                            .withReadTimeout(READ_TIMEOUT)))
            .build();

    /** @return 바꾼 줄 수 */
    @Transactional
    public int translateCityNames() {
        List<CountryInfoEntity> all = countryInfoRepository.findAll();
        int changed = 0;

        for (CountryInfoEntity country : all) {
            String city = country.getCityName();
            if (city == null || city.isBlank() || isKorean(city)) {
                continue;
            }
            String korean = lookupKorean(city, country.getCountryName());
            if (korean == null || korean.equals(city)) {
                continue;
            }
            log.info("{} : {} → {}", country.getCountryName(), city, korean);
            country.setCityName(korean);
            changed++;
        }
        return changed;
    }

    /** 한글이 한 자라도 있으면 이미 번역된 것으로 본다 */
    private boolean isKorean(String value) {
        return value.chars().anyMatch(c -> c >= 0xAC00 && c <= 0xD7A3);
    }

    private String lookupKorean(String cityName, String countryName) {
        try {
            JsonNode body = restClient.post()
                    .uri(SEARCH_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-Goog-Api-Key", apiKey)
                    .header("X-Goog-FieldMask", "places.displayName")
                    .body(Map.of(
                            // 나라를 같이 줘야 같은 이름의 다른 도시를 안 집는다
                            "textQuery", cityName + " " + countryName,
                            "languageCode", "ko",
                            "maxResultCount", 1))
                    .retrieve()
                    .body(JsonNode.class);

            JsonNode places = body.path("places");
            if (!places.isArray() || places.isEmpty()) {
                return null;
            }
            String name = places.get(0).path("displayName").path("text").asText(null);
            // 한글이 안 섞여 있으면 번역이 안 된 것이라 덮지 않는다
            return name != null && isKorean(name) ? name : null;
        } catch (Exception e) {
            log.warn("{} 도시 이름 조회 실패: {}", cityName, e.getMessage());
            return null;
        }
    }
}
