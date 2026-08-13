package com.example.PartTrip.domain.main.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "food_info")
@Getter
@Setter
@NoArgsConstructor
public class FoodInfoEntity {

    // 대표 음식 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_info_id")
    private Long foodInfoId;

    // 어느 나라 음식인지
    @Column(name = "country_name", nullable = false)
    private String countryName;

    // 음식 이름
    @Column(name = "food_name", nullable = false)
    private String foodName;

    // 음식 설명
    @Column(name = "description", length = 1000)
    private String description;

    // 음식 이미지 URL
    @Column(name = "image_url", length = 1000)
    private String imageUrl;
}