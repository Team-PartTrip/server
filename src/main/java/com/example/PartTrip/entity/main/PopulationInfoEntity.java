package com.example.PartTrip.entity.main;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "population_info")
@Getter
@Setter
public class PopulationInfoEntity {

    // 여행지 정보 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long populationInfoId;

    // 어느 나라의 인구 구성인지
    @Column(nullable = false)
    private String countryName;

    // 국기 코드 (CN, MY, IN ...)
    @Column(nullable = false)
    private String nationCode;

    // 민족 이름
    @Column(nullable = false)
    private String nationName;

    // 구성 비율(%)
    @Column(nullable = false)
    private Integer percent;

}