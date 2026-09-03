package com.example.PartTrip.tripcard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// 여행 카드의 방문 장소 (Func-003-02)
// 앱 D10 타임라인에서 날짜 아래에 붙는 장소 노드
@Entity
@Table(
        name = "trip_card_place",
        indexes = @Index(name = "idx_trip_card_place_card", columnList = "trip_card_id")
)
@Getter
@Setter
@NoArgsConstructor
public class TripCardPlaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_card_place_id")
    private Long tripCardPlaceId;

    @Column(name = "trip_card_id", nullable = false)
    private Long tripCardId;

    // tour_place 에 있는 장소면 연결된다. 사진 위치로만 잡힌 장소는 null
    @Column(name = "tour_place_id")
    private Long tourPlaceId;

    @Column(name = "place_name", nullable = false)
    private String placeName;

    @Column(name = "address", length = 500)
    private String address;

    // 타임라인 날짜 묶음의 기준
    @Column(name = "visited_date", nullable = false)
    private LocalDate visitedDate;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    // 같은 날짜 안에서의 순서
    @Column(name = "sort_order")
    private Integer sortOrder;
}
