package com.example.PartTrip.community.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trip_place")
@Getter
@Setter
@NoArgsConstructor
public class TripPlaceEntity {

    // 일정 장소 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_place_id")
    private Long tripPlaceId;

    // 소속 일정 (trip FK)
    @Column(name = "trip_id", nullable = false)
    private Long tripId;

    // 며칠차 (1일차, 2일차 ...)
    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    // 장소 이름
    @Column(name = "place_name", nullable = false)
    private String placeName;

    // 부가 설명 (지역명 등)
    @Column(name = "place_sub")
    private String placeSub;

    // 같은 날 안에서의 정렬 순서
    @Column(name = "sort_order")
    private Integer sortOrder;
}
