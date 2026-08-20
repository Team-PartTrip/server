package com.example.PartTrip.community.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
public class ReviewEntity {

    // 리뷰 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    // 리뷰 대상 여행지 (country_info FK)
    @Column(name = "country_info_id", nullable = false)
    private Long countryInfoId;

    // 작성자 ID
    @Column(name = "user_id", nullable = false)
    private String userId;

    // 리뷰 제목
    @Column(name = "title", nullable = false)
    private String title;

    // 별점 (1~5)
    @Column(name = "rating", nullable = false)
    private Integer rating;

    // 짧은 리뷰 내용
    @Column(name = "content", nullable = false, length = 300)
    private String content;

    // 작성일
    @Column(name = "create_date")
    private LocalDateTime createDate;
}
