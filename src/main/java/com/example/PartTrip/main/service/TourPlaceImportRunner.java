package com.example.PartTrip.main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 관광지 다시 채우기 실행부.
 *
 * 데이터를 지우고 새로 넣는 작업이라 평소에는 절대 돌면 안 된다.
 * 속성을 직접 준 실행에서만 만들어진다.
 *
 *   ./gradlew bootRun --args='--parttrip.places.import=일본/오사카,베트남/다낭'
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "parttrip.places.import")
public class TourPlaceImportRunner implements ApplicationRunner {

    private final TourPlaceImportService tourPlaceImportService;

    @Override
    public void run(ApplicationArguments args) {
        List<String> cities = args.getOptionValues("parttrip.places.import").stream()
                .flatMap(value -> Arrays.stream(value.split(",")))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .toList();

        log.info("관광지 다시 채우기 시작: {}", cities);
        Map<String, Integer> saved = tourPlaceImportService.importCities(cities);
        saved.forEach((city, count) -> log.info("  {} → {}개", city, count));
        log.info("관광지 다시 채우기 끝");
    }
}
