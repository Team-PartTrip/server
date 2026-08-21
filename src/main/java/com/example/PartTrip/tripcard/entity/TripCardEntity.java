package com.example.PartTrip.tripcard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 여행 카드 (Func-003)
//
// 앱 D9 목록 화면의 "함께한 사람 4명 / 방문 장소 11곳 / 남긴 사진 24장 /
// 이동 거리 86km" 가 이 한 행에 해당한다.
// 집계값은 매번 세지 않고 카드에 들고 있는다.
@Entity
@Table(name = "trip_card")
@Getter
@Setter
@NoArgsConstructor
public class TripCardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_card_id")
    private Long tripCardId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    // 플래너(Func-008)로 만든 여행이면 연결된다. 직접 만든 카드는 null
    @Column(name = "plan_id")
    private Long planId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "country_name", nullable = false, length = 100)
    private String countryName;

    @Column(name = "city_name", length = 100)
    private String cityName;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "cover_image_url", length = 1000)
    private String coverImageUrl;

    // D9 통계 — 함께한 사람 / 방문 장소 / 남긴 사진 / 이동 거리
    @Column(name = "companion_count")
    private Integer companionCount;

    @Column(name = "place_count")
    private Integer placeCount;

    @Column(name = "photo_count")
    private Integer photoCount;

    @Column(name = "distance_km")
    private Double distanceKm;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
