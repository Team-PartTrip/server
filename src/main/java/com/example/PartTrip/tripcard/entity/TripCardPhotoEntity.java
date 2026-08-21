package com.example.PartTrip.tripcard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// 여행 카드에 담긴 사진 (Func-003-03)
//
// 앱 D11 은 갤러리에서 사진을 골라 담고 코멘트를 남긴다.
// 해설 카메라(Func-005)로 찍은 사진이면 photo_id 로 연결한다.
@Entity
@Table(name = "trip_card_photo")
@Getter
@Setter
@NoArgsConstructor
public class TripCardPhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_card_photo_id")
    private Long tripCardPhotoId;

    @Column(name = "trip_card_id", nullable = false)
    private Long tripCardId;

    // 어느 장소에서 찍었는지. 위치를 못 잡은 사진은 null
    @Column(name = "trip_card_place_id")
    private Long tripCardPlaceId;

    // photo_manage 에 있는 해설 카메라 사진이면 연결된다
    @Column(name = "photo_id")
    private Long photoId;

    @Column(name = "image_url", nullable = false, length = 1000)
    private String imageUrl;

    // 사진 메타데이터의 촬영 시각 — 타임라인 정렬 기준
    @Column(name = "taken_at")
    private LocalDateTime takenAt;

    @Column(name = "comment", length = 500)
    private String comment;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "sort_order")
    private Integer sortOrder;
}
