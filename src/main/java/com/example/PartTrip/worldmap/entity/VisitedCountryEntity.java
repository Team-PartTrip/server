package com.example.PartTrip.worldmap.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 방문 국가 — 세계지도 땅따먹기 (API-006)
//
// 명세서 API-006-02: "이미 획득한 국가는 중복 등록하지 않는다"
// → (user_id, country_info_id) 를 유니크로 두어 DB 가 보장한다.
//
// visit_count 와 방문일로 API-006-03(국가별 기록), API-006-04(달성 현황)까지 커버한다.
@Entity
@Table(
        name = "visited_country",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_visited_country_user_country",
                columnNames = {"user_id", "country_info_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class VisitedCountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visited_country_id")
    private Long visitedCountryId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "country_info_id", nullable = false)
    private Long countryInfoId;

    // 처음 획득한 날 — 앱 E3 "일본을 획득했어요!" 시점
    @Column(name = "first_visited_at", nullable = false)
    private LocalDate firstVisitedAt;

    @Column(name = "last_visited_at")
    private LocalDate lastVisitedAt;

    // 같은 나라를 여러 번 갔을 때의 횟수 (Func-009-03 "방문 횟수")
    @Column(name = "visit_count", nullable = false)
    private Integer visitCount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
