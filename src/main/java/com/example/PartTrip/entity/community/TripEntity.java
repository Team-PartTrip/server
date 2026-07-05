package com.example.PartTrip.entity.community;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "trip")
@Getter
@Setter
@NoArgsConstructor
public class TripEntity {

    // 일정 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Long tripId;

    // 작성자(소유자) ID
    @Column(name = "user_id", nullable = false)
    private String userId;

    // 일정 제목
    @Column(name = "title", nullable = false)
    private String title;

    // 여행 국가/도시 (country_info FK)
    @Column(name = "country_info_id", nullable = false)
    private Long countryInfoId;

    // 여행 시작일
    @Column(name = "start_date")
    private LocalDate startDate;

    // 여행 종료일
    @Column(name = "end_date")
    private LocalDate endDate;

    // 여행 이야기(서술형 내용)
    @Column(name = "content", length = 2000)
    private String content;

    // 커뮤니티 공개 여부 (공유했는지 여부)
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false;

    // 작성일
    @Column(name = "create_date")
    private LocalDateTime createDate;
}
