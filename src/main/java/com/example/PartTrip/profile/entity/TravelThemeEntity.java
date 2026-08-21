package com.example.PartTrip.profile.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 여행 타입 (Func-007-01)
// 프로필에 "계획형 모험가" 처럼 표시되는 사용자 성향 마스터 데이터
@Entity
@Table(name = "travel_theme")
@Getter
@NoArgsConstructor
public class TravelThemeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theme_id")
    private Long themeId;

    // 코드값 (예: PLANNER_ADVENTURER)
    @Column(name = "theme_code", nullable = false, unique = true, length = 30)
    private String themeCode;

    // 화면에 보여줄 이름 (예: 계획형 모험가)
    @Column(name = "theme_name", nullable = false, length = 50)
    private String themeName;

    // 한 줄 설명 (예: 여행을 계획하고 기록하는 사람)
    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "create_date")
    private java.time.LocalDateTime createDate;
}
