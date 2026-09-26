package com.example.PartTrip.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

// @Scheduled 를 켠다. 쓰는 곳은 세 군데다.
//   TripCardScheduler           매일 01:00 여행 종료 처리
//   LocationService             매시 오래된 위치 삭제
//   KoreaFestivalImportService  매일 04:30 축제 동기화
@Configuration
@EnableScheduling
public class SchedulingConfig {
}
