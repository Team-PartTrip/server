package com.example.PartTrip.main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.hasText;

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
// 둘 중 하나만 줘도 돈다. 번역만 하려고 관광지를 괜히 다시 받지 않게 한다.
@ConditionalOnExpression(
        "'${parttrip.places.import:}' != ''"
                + " or '${parttrip.places.translate-cities:}' != ''"
                + " or '${parttrip.places.translate-addresses:}' != ''")
public class TourPlaceImportRunner implements ApplicationRunner {

    private final TourPlaceImportService tourPlaceImportService;
    private final CountryCityNameService countryCityNameService;
    private final TourPlaceAddressService tourPlaceAddressService;
    /**
     * 빈을 만들지 말지는 @ConditionalOnExpression 이 Environment 를 보고 정한다.
     * 여기서 명령줄 옵션만 보면, application.properties 나 환경변수로 준
     * 경우에 빈은 만들어지고 아무 일도 안 하는 상태가 된다. 같은 곳을 본다.
     */
    private final Environment environment;

    @Override
    public void run(ApplicationArguments args) {
        String importValue = environment.getProperty("parttrip.places.import");
        if (hasText(importValue)) {
            List<String> cities = Arrays.stream(importValue.split(","))
                    .map(String::trim)
                    .filter(value -> !value.isBlank())
                    .toList();

            log.info("관광지 다시 채우기 시작: {}", cities);
            Map<String, Integer> saved = tourPlaceImportService.importCities(cities);
            saved.forEach((city, count) -> log.info("  {} → {}개", city, count));
            log.info("관광지 다시 채우기 끝");
        }

        // 도시 이름 한글화도 같은 실행에서 함께 할 수 있게 둔다
        if (hasText(environment.getProperty("parttrip.places.translate-cities"))) {
            log.info("도시 이름 한글화 시작");
            log.info("도시 이름 한글화 끝: {}줄", countryCityNameService.translateCityNames());
        }

        if (hasText(environment.getProperty("parttrip.places.translate-addresses"))) {
            log.info("관광지 주소 한글화 시작");
            log.info("관광지 주소 한글화 끝: {}줄", tourPlaceAddressService.translateAddresses());
        }
    }
}
