package com.example.PartTrip.planner.repository;

import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.enums.VoteStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class VoteRepositoryTest {

    @Autowired private GroupTravelPlanRepository groupTravelPlanRepository;
    @Autowired private VoteRepository voteRepository;

    @Test
    void createdAt이_같으면_planId가_큰_계획의_투표만_조회한다() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 9, 2, 12, 0);
        GroupTravelPlanEntity firstPlan = groupTravelPlanRepository.saveAndFlush(
                plan(7L, "오사카", createdAt));
        GroupTravelPlanEntity secondPlan = groupTravelPlanRepository.saveAndFlush(
                plan(7L, "도쿄", createdAt));
        VoteEntity firstVote = voteRepository.saveAndFlush(
                vote(firstPlan.getPlanId(), TourPlaceCategory.CAFE, createdAt));
        VoteEntity secondVote = voteRepository.saveAndFlush(
                vote(secondPlan.getPlanId(), TourPlaceCategory.ATTRACTION, createdAt));

        assertThat(secondPlan.getPlanId()).isGreaterThan(firstPlan.getPlanId());
        assertThat(voteRepository.findLatestPlanVotes(7L))
                .extracting(VoteEntity::getVoteId)
                .containsExactly(secondVote.getVoteId())
                .doesNotContain(firstVote.getVoteId());
    }

    private GroupTravelPlanEntity plan(Long groupId, String cityName, LocalDateTime createdAt) {
        GroupTravelPlanEntity plan = new GroupTravelPlanEntity();
        plan.setGroupId(groupId);
        plan.setCountryName("일본");
        plan.setCityName(cityName);
        plan.setStartDate(LocalDate.of(2026, 10, 1));
        plan.setEndDate(LocalDate.of(2026, 10, 5));
        plan.setCreatedAt(createdAt);
        return plan;
    }

    private VoteEntity vote(
            Long planId,
            TourPlaceCategory category,
            LocalDateTime createdAt
    ) {
        VoteEntity vote = new VoteEntity();
        vote.setPlanId(planId);
        vote.setCategory(category);
        vote.setStatus(VoteStatus.OPEN);
        vote.setCreatedAt(createdAt);
        return vote;
    }
}
