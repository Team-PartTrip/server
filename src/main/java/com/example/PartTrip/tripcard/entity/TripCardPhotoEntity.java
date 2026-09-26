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
@Table(
        name = "trip_card_photo",
        // 카드 상세는 카드 단위로, 장소별 사진은 place 단위로 긁는다
        indexes = {
                @Index(name = "idx_trip_card_photo_card", columnList = "trip_card_id"),
                @Index(name = "idx_trip_card_photo_place", columnList = "trip_card_place_id")
        }
)
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

    // 지도에서 직접 고른 장소 이름. EXIF 좌표로 올라온 사진은 비어 있다.
    // 좌표만 있으면 앱·웹이 "사진 촬영 위치" 로만 보여줘서 어디였는지 남지 않는다.
    @Column(name = "place_name")
    private String placeName;

    // 좌표와 촬영 시각이 각각 어디서 왔는지. 비어 있으면 아직 값이 없다는 뜻이다.
    // EXIF 면 잠그고, 비었거나 MANUAL 이면 기한 없이 다시 고를 수 있다 (Func-003-07).
    @Enumerated(EnumType.STRING)
    @Column(name = "location_source", length = 16)
    private MetadataSource locationSource;

    @Enumerated(EnumType.STRING)
    @Column(name = "taken_at_source", length = 16)
    private MetadataSource takenAtSource;
}
