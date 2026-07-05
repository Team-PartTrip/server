package com.example.PartTrip.entity.main;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tour_place")
@Getter
@Setter
@NoArgsConstructor
public class TourPlaceEntity {

    // 관광지 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_place_id")
    private Long tourPlaceId;

    // 어느 나라 관광지인지
    @Column(name = "country_name", nullable = false)
    private String countryName;

    // 관광지 이름
    @Column(name = "place_name", nullable = false)
    private String placeName;

    // 관광지 설명
    @Column(name = "description", length = 1000)
    private String description;

    // 관광지 이미지
    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    // 위도
    @Column(name = "latitude")
    private Double latitude;

    // 경도
    @Column(name = "longitude")
    private Double longitude;

}