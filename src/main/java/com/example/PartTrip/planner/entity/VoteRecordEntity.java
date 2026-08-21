package com.example.PartTrip.planner.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// 투표 기록 — 누가 무엇을 골랐는지 (Func-008-05)
//
// (vote_id, user_id) 를 유니크로 두어 한 투표에 1인 1표를 보장한다.
// 표를 바꾸는 경우 새로 넣지 말고 기존 행의 option_id 를 수정한다.
@Entity
@Table(
        name = "vote_record",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_vote_record_vote_user",
                columnNames = {"vote_id", "user_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class VoteRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_record_id")
    private Long voteRecordId;

    @Column(name = "vote_id", nullable = false)
    private Long voteId;

    @Column(name = "option_id", nullable = false)
    private Long optionId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "voted_at", nullable = false)
    private LocalDateTime votedAt;
}
