package com.example.PartTrip.main.entity;

import com.example.PartTrip.main.enums.TourPlaceCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "tour_place",
        // 메인 추천 · 장소 목록 · 인기 도시가 모두 이 두 컬럼으로 찾는다
        indexes = @Index(
                name = "idx_tour_place_country_city",
                columnList = "country_name, city_name")
)
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

    // 어느 도시 관광지인지
    @Column(name = "city_name")
    private String cityName;

    // 관광지 이름
    @Column(name = "place_name", nullable = false)
    private String placeName;

    // 카테고리 (맛집 / 명소 / 숙소 / 카페 / 액티비티 / 쇼핑)
    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 30)
    private TourPlaceCategory category;

    // 관광지 설명
    @Column(name = "description", length = 1000)
    private String description;

    // 주소 (앱 목록에서 지역 표기에 사용)
    @Column(name = "address", length = 500)
    private String address;

    // 평점 (0.0 ~ 5.0)
    @Column(name = "rating")
    private Double rating;

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
