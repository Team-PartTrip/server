package com.example.PartTrip.planner.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 한 플래너가 도는 도시들 — 도시마다 며칠씩 (Func-005-02)
 *
 * group_travel_plan 에는 city_name 이 한 칸뿐이라 오사카·교토를 같이 갈 수
 * 없었다. 도시를 여기에 줄로 쌓고, group_travel_plan.city_name 에는 첫 도시를
 * 그대로 둔다. 그래야 도시 하나만 보던 기존 코드가 안 깨진다.
 *
 * 날짜를 도시마다 나눠 갖는 이유는 AI 가 어느 날을 어느 도시에서 짤지
 * 알아야 하기 때문이다. 모르면 오사카 맛집과 교토 명소를 같은 날에 묶는다.
 */
@Entity
@Table(
        name = "planner_city",
        // 플래너를 열 때마다 plan_id 로 도시를 통째로 읽는다
        indexes = @Index(name = "idx_planner_city_plan", columnList = "plan_id")
)
@Getter
@Setter
@NoArgsConstructor
public class PlannerCityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "planner_city_id")
    private Long plannerCityId;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

    // 방문 순서. 0 이 첫 도시다
    @Column(name = "seq", nullable = false)
    private int seq;

    @Column(name = "region_code", nullable = false, length = 2)
    private String regionCode;

    @Column(name = "city_name", nullable = false, length = 100)
    private String cityName;

    // 이 도시에 머무는 기간. 이동하는 날은 그날 묵는 도시에 넣는다
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}
