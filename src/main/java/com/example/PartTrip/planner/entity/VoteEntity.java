package com.example.PartTrip.planner.entity;

import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.planner.enums.VoteStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// 카테고리별 투표 (Func-008-03)
//
// 한 여행 계획에서 카테고리마다 투표를 한 번씩 연다.
// 앱 C5 의 "맛집 투표 · 3/4명 참여 · 오늘 21:00 마감" 이 이 한 행에 해당한다.
@Entity
@Table(
        name = "vote",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_vote_plan_category",
                columnNames = {"plan_id", "category"})
)
@Getter
@Setter
@NoArgsConstructor
public class VoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long voteId;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

    // Func-008-04 의 장소 카테고리를 그대로 쓴다
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private TourPlaceCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private VoteStatus status;

    // 앱 C5 의 "오늘 21:00 마감". 마감이 없는 투표도 허용한다
    @Column(name = "deadline")
    private LocalDateTime deadline;

    // 확정된 후보 (vote_option.option_id). 확정 전에는 null
    @Column(name = "confirmed_option_id")
    private Long confirmedOptionId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
