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

    // 스케줄러는 매시 한 시간짜리 구간으로 훑는다. 구간을 양끝 다 닫으면
    // 앞 실행의 끝과 다음 실행의 시작이 같은 시각이라 그 경계의 투표가
    // 두 번 잡히고, 발송 기록이 없으니 알림도 두 번 나간다.
    @Test
    void 경계_시각의_투표는_한_구간에서만_잡힌다() {
        LocalDateTime boundary = LocalDateTime.of(2026, 9, 3, 12, 0);
        VoteEntity onBoundary = voteRepository.saveAndFlush(
                voteWithDeadline(1L, TourPlaceCategory.CAFE, boundary));

        // 앞 실행: 11:00 ~ 12:00 — to 라서 빠져야 한다
        assertThat(voteRepository
                .findByStatusAndDeadlineGreaterThanEqualAndDeadlineLessThan(
                        VoteStatus.OPEN, boundary.minusHours(1), boundary))
                .doesNotContain(onBoundary);

        // 다음 실행: 12:00 ~ 13:00 — from 이라서 여기서만 잡힌다
        assertThat(voteRepository
                .findByStatusAndDeadlineGreaterThanEqualAndDeadlineLessThan(
                        VoteStatus.OPEN, boundary, boundary.plusHours(1)))
                .extracting(VoteEntity::getVoteId)
                .containsExactly(onBoundary.getVoteId());
    }

    private VoteEntity voteWithDeadline(
            Long planId,
            TourPlaceCategory category,
            LocalDateTime deadline
    ) {
        VoteEntity vote = vote(planId, category, deadline.minusDays(1));
        vote.setDeadline(deadline);
        return vote;
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
