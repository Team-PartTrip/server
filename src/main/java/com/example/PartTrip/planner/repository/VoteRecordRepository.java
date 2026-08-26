package com.example.PartTrip.planner.repository;

import com.example.PartTrip.planner.entity.VoteRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VoteRecordRepository extends JpaRepository<VoteRecordEntity, Long> {

    List<VoteRecordEntity> findByVoteId(Long voteId);

    List<VoteRecordEntity> findByVoteIdIn(List<Long> voteIds);

    // 1인 1표라 최대 한 건이다. 표를 바꾸면 이 행의 option_id 를 수정한다
    Optional<VoteRecordEntity> findByVoteIdAndUserId(Long voteId, String userId);

    // 앱 C5 의 "3 / 4명 참여"
    long countByVoteId(Long voteId);

    // 후보별 득표 수 — [optionId, count] 형태로 돌아온다
    @Query("""
            SELECT r.optionId, COUNT(r)
            FROM VoteRecordEntity r
            WHERE r.voteId = :voteId
            GROUP BY r.optionId
            ORDER BY COUNT(r) DESC
            """)
    List<Object[]> countByOption(Long voteId);

    void deleteByVoteId(Long voteId);
}
