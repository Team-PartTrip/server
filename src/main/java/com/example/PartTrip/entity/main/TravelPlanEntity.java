package com.example.PartTrip.entity.main;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "travel_plan")
@Getter
@Setter
@NoArgsConstructor
public class TravelPlanEntity {

    // 여행 일정 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_plan_id")
    private Long travelPlanId;

    // 사용자 아이디
    @Column(name = "user_id", nullable = false)
    private String userId;

    // 여행 국가
    @Column(name = "country_name", nullable = false)
    private String countryName;

    // 여행 도시
    @Column(name = "city_name", nullable = false)
    private String cityName;

    // 여행 시작일
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    // 여행 종료일
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}