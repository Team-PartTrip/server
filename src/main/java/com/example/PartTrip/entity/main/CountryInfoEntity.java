package com.example.PartTrip.entity.main;

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

    // 통화 코드 (ISO 4217, 예: USD, KRW) - 환율 조회에 사용
    @Column(name = "currency_code")
    private String currencyCode;

    // 위도/경도 - 실시간 날씨 조회에 사용
    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

//    // 환율 표시
//    @Column(name = "exchange_rate_text")
//    private String exchangeRateText;
//
//    // 현지 시간
//    @Column(name = "local_time_text")
//    private String localTimeText;

    // 문화 요약
    @Column(name = "summary", length = 1000)
    private String summary;

}
