package com.example.PartTrip.planner.repository;

import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.GroupStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GroupTravelPlanRepositoryTest {

    @Autowired private TravelGroupRepository travelGroupRepository;
    @Autowired private GroupMemberRepository groupMemberRepository;
    @Autowired private GroupTravelPlanRepository groupTravelPlanRepository;

    @Test
    void touchingStartOrEndDateCountsAsOverlap() {
        saveGroupWithMemberAndPlan(
                "user", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30));

        assertThat(groupTravelPlanRepository.existsOverlappingPlanForUser(
                "user", LocalDate.of(2026, 8, 31), LocalDate.of(2026, 9, 1))).isTrue();
        assertThat(groupTravelPlanRepository.existsOverlappingPlanForUser(
                "user", LocalDate.of(2026, 9, 30), LocalDate.of(2026, 10, 1))).isTrue();
        assertThat(groupTravelPlanRepository.existsOverlappingPlanForUser(
                "user", LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2))).isFalse();
    }

    @Test
    void detectsConflictForAnyCurrentGroupMember() {
        saveGroupWithMemberAndPlan(
                "member", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30));
        TravelGroupEntity targetGroup = saveGroup("target-owner");
        saveMember(targetGroup.getGroupId(), "member");

        assertThat(groupTravelPlanRepository.existsOverlappingPlanForGroupMembersExcludingGroup(
                targetGroup.getGroupId(),
                LocalDate.of(2026, 9, 15),
                LocalDate.of(2026, 9, 20))).isTrue();
    }

    private Long saveGroupWithMemberAndPlan(
            String userId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        TravelGroupEntity group = saveGroup(userId);
        saveMember(group.getGroupId(), userId);
        GroupTravelPlanEntity plan = new GroupTravelPlanEntity();
        plan.setGroupId(group.getGroupId());
        plan.setCountryName("일본");
        plan.setCityName("오사카");
        plan.setStartDate(startDate);
        plan.setEndDate(endDate);
        plan.setCreatedAt(LocalDateTime.now());
        groupTravelPlanRepository.saveAndFlush(plan);
        return group.getGroupId();
    }

    private TravelGroupEntity saveGroup(String ownerId) {
        TravelGroupEntity group = new TravelGroupEntity();
        group.setOwnerUserId(ownerId);
        group.setGroupName("여행");
        group.setHeadcount(2);
        group.setStatus(GroupStatus.PLANNING);
        group.setCreatedAt(LocalDateTime.now());
        return travelGroupRepository.saveAndFlush(group);
    }

    private void saveMember(Long groupId, String userId) {
        GroupMemberEntity member = new GroupMemberEntity();
        member.setGroupId(groupId);
        member.setUserId(userId);
        member.setRole(GroupRole.MEMBER);
        member.setJoinedAt(LocalDateTime.now());
        groupMemberRepository.saveAndFlush(member);
    }
}
