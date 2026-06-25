package com.example.PartTrip.entity.home;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "country_info")
@Getter
@Setter
@NoArgsConstructor
public class CountryInfoEntity {

    // 국가 정보 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_info_id")
    private Long countryInfoId;

    // 나라 이름
    @Column(name = "country_name", nullable = false)
    private String countryName;

    // 도시 이름
    @Column(name = "city_name", nullable = false)
    private String cityName;

    // 메인 사진 주소
    @Column(name = "image_url")
    private String imageUrl;

    // 환율 표시
    @Column(name = "exchange_rate_text")
    private String exchangeRateText;

    // 현지 시간
    @Column(name = "local_time_text")
    private String localTimeText;

    // 문화 요약
    @Column(name = "summary", length = 1000)
    private String summary;

}
