package com.example.PartTrip.service.main;

import com.example.PartTrip.dto.main.CalendarFestivalDto;
import com.example.PartTrip.entity.main.CountryInfoEntity;
import com.example.PartTrip.repository.main.CountryInfoRepository;
import com.example.PartTrip.util.CountryCodeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CountryInfoRepository countryInfoRepository;
    private static final RestTemplate restTemplate = new RestTemplate();

    @Value("${ticketmaster.api.key}")
    private String apiKey;

    @Value("${ticketmaster.api.base-url}")
    private String baseUrl;

    @SuppressWarnings("unchecked")
    public List<CalendarFestivalDto> getFestivals(Long countryInfoId, int year, int month) {

        CountryInfoEntity country = countryInfoRepository.findById(countryInfoId)
                .orElseThrow(() -> new IllegalArgumentException("국가 정보를 찾을 수 없습니다."));

        String countryCode = CountryCodeMapper.getCountryCode(country.getCountryName());
        if (countryCode == null) {
            log.warn("매핑되지 않은 국가: {}", country.getCountryName());
            return Collections.emptyList();
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/events.json")
                .queryParam("apikey", apiKey)
                .queryParam("countryCode", countryCode)
                .queryParam("classificationName", "festival")
                .queryParam("startDateTime", startDate + "T00:00:00Z")
                .queryParam("endDateTime", endDate + "T23:59:59Z")
                .queryParam("size", 50)
                .queryParam("sort", "date,asc")
                .toUriString();

        try {
            ResponseEntity<Map> responseEntity = restTemplate.getForEntity(url, Map.class);

            if (responseEntity.getBody() == null) {
                return Collections.emptyList();
            }

            Map<String, Object> response = responseEntity.getBody();
            Map<String, Object> embedded = (Map<String, Object>) response.get("_embedded");
            if (embedded == null) {
                return Collections.emptyList();
            }

            List<Map<String, Object>> events = (List<Map<String, Object>>) embedded.get("events");
            if (events == null) {
                return Collections.emptyList();
            }

            List<CalendarFestivalDto> result = new ArrayList<>();

            for (Map<String, Object> event : events) {
                try {
                    result.add(parseEvent(event));
                } catch (Exception e) {
                    log.warn("이벤트 파싱 실패: {}", event.get("id"), e);
                }
            }

            return result;

        } catch (Exception e) {
            log.error("Ticketmaster API 호출 실패 - countryCode={}, year={}, month={}", countryCode, year, month, e);
            return Collections.emptyList();
        }
    }

    @SuppressWarnings("unchecked")
    private CalendarFestivalDto parseEvent(Map<String, Object> event) {

        String name = (String) event.get("name");
        String description = (String) event.get("info");

        Map<String, Object> dates = (Map<String, Object>) event.get("dates");
        Map<String, Object> start = (Map<String, Object>) dates.get("start");
        String startDateStr = (String) start.get("localDate");

        Map<String, Object> end = (Map<String, Object>) dates.get("end");
        String endDateStr = (end != null) ? (String) end.get("localDate") : startDateStr;

        return new CalendarFestivalDto(
                name,
                description,
                startDateStr,
                endDateStr
        );
    }
}
