package com.example.PartTrip.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

// @Scheduled 를 켠다. 현재는 투표 마감 임박 알림에서만 쓴다.
@Configuration
@EnableScheduling
public class SchedulingConfig {
}
