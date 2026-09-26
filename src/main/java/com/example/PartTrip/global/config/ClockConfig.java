package com.example.PartTrip.global.config;

import com.example.PartTrip.PartTripApplication;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** 국내 여행의 날짜 기준을 서버 실행 환경과 무관하게 유지한다. */
@Configuration
public class ClockConfig {
    @Bean
    public Clock applicationClock() {
        return Clock.system(PartTripApplication.ZONE);
    }
}
