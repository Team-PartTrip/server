package com.example.PartTrip.entity.main;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "festival")
@Getter
@Setter
public class FestivalEntity {

    // 축제 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long festivalId;

    // 어느 나라 축제인지
    @Column(nullable = false)
    private String countryName;

    // 축제 이름
    @Column(nullable = false)
    private String title;

    // 축제 카테고리
    @Column(nullable = false)
    private String category;

    // 축제 설명
    @Column(nullable = false, length = 500)
    private String description;

    // 시작 날짜
    @Column(nullable = false)
    private String startDate;

    // 시작 시간
    @Column(nullable = false)
    private String startTime;

    // 장소
    @Column(nullable = false)
    private String location;

    // 축제 이미지 URL
    @Column(nullable = false, length = 1000)
    private String imageUrl;
}