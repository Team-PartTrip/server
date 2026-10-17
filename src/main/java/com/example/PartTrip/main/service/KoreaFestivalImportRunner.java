package com.example.PartTrip.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "parttrip.festivals.import", havingValue = "true")
public class KoreaFestivalImportRunner implements ApplicationRunner {

    private final KoreaFestivalImportService koreaFestivalImportService;

    @Override
    public void run(ApplicationArguments args) {
        koreaFestivalImportService.importFrom(LocalDate.now().minusMonths(1).withDayOfMonth(1));
    }
}
