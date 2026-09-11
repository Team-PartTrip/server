package com.example.PartTrip.planner.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "vote_record",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_vote_record_option_user",
                columnNames = {"option_id", "user_id"}),
        indexes = @Index(name = "idx_vote_record_vote", columnList = "vote_id")
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
