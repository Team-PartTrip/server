package com.example.PartTrip.planner.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// 투표 후보 — 장바구니에 담긴 장소 (Func-008-03-1)
@Entity
@Table(
        name = "vote_option",
        // 투표 화면을 열 때마다 vote_id 로 후보를 긁는다
        indexes = @Index(name = "idx_vote_option_vote", columnList = "vote_id")
)
@Getter
@Setter
@NoArgsConstructor
public class VoteOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long optionId;

    @Column(name = "vote_id", nullable = false)
    private Long voteId;

    // Func-008-04 장소 조회에서 담은 경우 연결된다.
    // 직접 입력한 후보는 null 이고 place_name 만 채워진다.
    @Column(name = "tour_place_id")
    private Long tourPlaceId;

    @Column(name = "place_name", nullable = false)
    private String placeName;

    // 이 후보를 담은 멤버
    @Column(name = "added_by_user_id")
    private String addedByUserId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
