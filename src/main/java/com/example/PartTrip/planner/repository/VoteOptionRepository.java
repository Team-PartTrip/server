package com.example.PartTrip.planner.repository;

import com.example.PartTrip.planner.entity.VoteOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteOptionRepository extends JpaRepository<VoteOptionEntity, Long> {

    List<VoteOptionEntity> findByVoteIdOrderByCreatedAtAsc(Long voteId);

    // 삭제 시 어느 투표의 후보인지까지 확인한다
    Optional<VoteOptionEntity> findByOptionIdAndVoteId(Long optionId, Long voteId);

    boolean existsByVoteIdAndTourPlaceId(Long voteId, Long tourPlaceId);

    void deleteByVoteId(Long voteId);
}
