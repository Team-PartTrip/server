package com.example.PartTrip.planner.entity;

import com.example.PartTrip.main.enums.TourPlaceCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 도시마다 카테고리별로 몇 곳을 확정할지 (Func-005-08)
 *
 * 예전에는 여행 일수로 계산했다. 4박 5일이면 맛집 10곳이 무조건 확정
 * 대상이라 팀이 원하는 여행 밀도와 상관없이 개수가 정해졌다. 그룹장이
 * 플래너를 만들 때 직접 정하도록 이 표에 저장한다.
 *
 * 도시마다 따로 정하므로 seq 로 도시를 구분한다. planner_city.seq 와 같은
 * 값이고, 도시를 나누지 않은 여행은 0 한 줄만 쓴다.
 *
 * 숙소는 여행 내내 같은 곳에 묵으므로 도시별로 쌓지 않고 seq 0 에 1 만
 * 둔다. 그래야 카테고리별 합이 곧 확정 개수가 된다.
 */
@Entity
@Table(
        name = "planner_category_count",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_planner_category_count",
                columnNames = {"plan_id", "seq", "category"}),
        // 투표를 마감할 때마다 plan_id 로 통째로 읽는다
        indexes = @Index(name = "idx_planner_category_count_plan", columnList = "plan_id")
)
@Getter
@Setter
@NoArgsConstructor
public class PlannerCategoryCountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "planner_category_count_id")
    private Long plannerCategoryCountId;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

    // 방문 순서. planner_city.seq 와 같다
    @Column(name = "seq", nullable = false)
    private int seq;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private TourPlaceCategory category;

    @Column(name = "required_count", nullable = false)
    private int requiredCount;
}
