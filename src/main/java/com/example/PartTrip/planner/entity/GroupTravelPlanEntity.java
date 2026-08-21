package com.example.PartTrip.planner.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 그룹 여행 계획 — 여행지와 기간 (Func-008-02)
//
// 개인 D-day 용 travel_plan(Func-002)과 이름이 겹치지 않도록
// group_travel_plan 으로 둔다. 둘은 서로 다른 테이블이다.
@Entity
@Table(name = "group_travel_plan")
@Getter
@Setter
@NoArgsConstructor
public class GroupTravelPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long planId;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "travel_title", length = 100)
    private String travelTitle;

    @Column(name = "country_name", nullable = false, length = 100)
    private String countryName;

    @Column(name = "city_name", nullable = false, length = 100)
    private String cityName;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
