package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.request.JoinPlannerRequestDto;
import com.example.PartTrip.planner.dto.request.CreatePlannerRequestDto;
import com.example.PartTrip.planner.dto.request.SavePlannerTravelPlanRequestDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.signup.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        PlannerMemberService.class,
        PlannerService.class,
        PlannerTravelPlanService.class,
        PlannerScheduleLockService.class,
        PlannerInviteLinkFactory.class
})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class PlannerConcurrentJoinIntegrationTest {

    @Autowired private PlannerMemberService plannerMemberService;
    @Autowired private PlannerService plannerService;
    @Autowired private PlannerTravelPlanService plannerTravelPlanService;
    @Autowired private UserRepository userRepository;
    @Autowired private TravelGroupRepository travelGroupRepository;
    @Autowired private GroupMemberRepository groupMemberRepository;
    @Autowired private GroupTravelPlanRepository groupTravelPlanRepository;

    @Test
    void concurrentJoinsToOverlappingPlannersAllowOnlyOne() throws Exception {
        String userId = "concurrent-user";
        userRepository.saveAndFlush(user(userId));
        savePlanner("owner-a", "INVITEA", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 10));
        savePlanner("owner-b", "INVITEB", LocalDate.of(2026, 9, 5), LocalDate.of(2026, 9, 15));

        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<JoinResult> first = executor.submit(
                    () -> joinAtSameTime("INVITEA", userId, ready, start));
            Future<JoinResult> second = executor.submit(
                    () -> joinAtSameTime("INVITEB", userId, ready, start));
            assertThat(ready.await(5, TimeUnit.SECONDS)).isTrue();
            start.countDown();

            List<JoinResult> results = List.of(
                    first.get(10, TimeUnit.SECONDS), second.get(10, TimeUnit.SECONDS));
            assertThat(results).filteredOn(JoinResult::success).hasSize(1);
            assertThat(results).filteredOn(result -> !result.success())
                    .extracting(JoinResult::message)
                    .containsExactly("해당 기간에 이미 등록된 여행 계획이 있습니다.");
        }

        assertThat(groupMemberRepository.findByUserId(userId)).hasSize(1);
    }

    @Test
    void concurrentOverlappingPlannerCreationsAllowOnlyOne() throws Exception {
        String userId = "concurrent-creator";
        userRepository.saveAndFlush(user(userId));
        CreatePlannerRequestDto firstRequest = createRequest(
                "오사카", LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 10));
        CreatePlannerRequestDto secondRequest = createRequest(
                "싱가포르", LocalDate.of(2026, 10, 5), LocalDate.of(2026, 10, 15));

        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<JoinResult> first = executor.submit(
                    () -> createAtSameTime(firstRequest, userId, ready, start));
            Future<JoinResult> second = executor.submit(
                    () -> createAtSameTime(secondRequest, userId, ready, start));
            assertThat(ready.await(5, TimeUnit.SECONDS)).isTrue();
            start.countDown();

            assertOneSuccessAndOneOverlapFailure(first, second);
        }

        assertThat(groupMemberRepository.findByUserId(userId)).hasSize(1);
    }

    @Test
    void concurrentOverlappingPlanUpdatesForSharedMemberAllowOnlyOne() throws Exception {
        String userId = "shared-owner";
        userRepository.saveAndFlush(user(userId));
        TravelGroupEntity firstGroup = saveGroupWithOwner(userId, "UPDATEA");
        TravelGroupEntity secondGroup = saveGroupWithOwner(userId, "UPDATEB");
        SavePlannerTravelPlanRequestDto firstRequest = saveRequest(
                "오사카", LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 10));
        SavePlannerTravelPlanRequestDto secondRequest = saveRequest(
                "싱가포르", LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 15));

        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<JoinResult> first = executor.submit(() -> saveAtSameTime(
                    firstGroup.getGroupId(), firstRequest, userId, ready, start));
            Future<JoinResult> second = executor.submit(() -> saveAtSameTime(
                    secondGroup.getGroupId(), secondRequest, userId, ready, start));
            assertThat(ready.await(5, TimeUnit.SECONDS)).isTrue();
            start.countDown();

            assertOneSuccessAndOneOverlapFailure(first, second);
        }

        long savedPlanCount = groupTravelPlanRepository.findAll().stream()
                .filter(plan -> plan.getGroupId().equals(firstGroup.getGroupId())
                        || plan.getGroupId().equals(secondGroup.getGroupId()))
                .count();
        assertThat(savedPlanCount).isEqualTo(1);
    }

    private void assertOneSuccessAndOneOverlapFailure(
            Future<JoinResult> first,
            Future<JoinResult> second
    ) throws Exception {
        List<JoinResult> results = List.of(
                first.get(10, TimeUnit.SECONDS), second.get(10, TimeUnit.SECONDS));
        assertThat(results).filteredOn(JoinResult::success).hasSize(1);
        assertThat(results).filteredOn(result -> !result.success())
                .extracting(JoinResult::message)
                .containsExactly("해당 기간에 이미 등록된 여행 계획이 있습니다.");
    }

    private JoinResult joinAtSameTime(
            String inviteCode,
            String userId,
            CountDownLatch ready,
            CountDownLatch start
    ) throws InterruptedException {
        ready.countDown();
        start.await();
        JoinPlannerRequestDto request = new JoinPlannerRequestDto();
        request.setInviteCode(inviteCode);
        try {
            plannerMemberService.joinPlanner(request, userId);
            return new JoinResult(true, null);
        } catch (IllegalArgumentException exception) {
            return new JoinResult(false, exception.getMessage());
        }
    }

    private JoinResult createAtSameTime(
            CreatePlannerRequestDto request,
            String userId,
            CountDownLatch ready,
            CountDownLatch start
    ) throws InterruptedException {
        ready.countDown();
        start.await();
        try {
            plannerService.createPlanner(request, userId);
            return new JoinResult(true, null);
        } catch (IllegalArgumentException exception) {
            return new JoinResult(false, exception.getMessage());
        }
    }

    private JoinResult saveAtSameTime(
            Long plannerId,
            SavePlannerTravelPlanRequestDto request,
            String userId,
            CountDownLatch ready,
            CountDownLatch start
    ) throws InterruptedException {
        ready.countDown();
        start.await();
        try {
            plannerTravelPlanService.saveTravelPlan(plannerId, request, userId);
            return new JoinResult(true, null);
        } catch (IllegalArgumentException exception) {
            return new JoinResult(false, exception.getMessage());
        }
    }

    private void savePlanner(
            String ownerId,
            String inviteCode,
            LocalDate startDate,
            LocalDate endDate
    ) {
        TravelGroupEntity group = new TravelGroupEntity();
        group.setOwnerUserId(ownerId);
        group.setGroupName(inviteCode);
        group.setHeadcount(2);
        group.setInviteCode(inviteCode);
        group.setStatus(GroupStatus.PLANNING);
        group.setCreatedAt(LocalDateTime.now());
        group = travelGroupRepository.saveAndFlush(group);

        GroupMemberEntity owner = new GroupMemberEntity();
        owner.setGroupId(group.getGroupId());
        owner.setUserId(ownerId);
        owner.setRole(GroupRole.OWNER);
        owner.setJoinedAt(LocalDateTime.now());
        groupMemberRepository.saveAndFlush(owner);

        GroupTravelPlanEntity plan = new GroupTravelPlanEntity();
        plan.setGroupId(group.getGroupId());
        plan.setCountryName("국가");
        plan.setCityName("도시");
        plan.setStartDate(startDate);
        plan.setEndDate(endDate);
        plan.setCreatedAt(LocalDateTime.now());
        groupTravelPlanRepository.saveAndFlush(plan);
    }

    private TravelGroupEntity saveGroupWithOwner(String ownerId, String inviteCode) {
        TravelGroupEntity group = new TravelGroupEntity();
        group.setOwnerUserId(ownerId);
        group.setGroupName(inviteCode);
        group.setHeadcount(2);
        group.setInviteCode(inviteCode);
        group.setStatus(GroupStatus.PLANNING);
        group.setCreatedAt(LocalDateTime.now());
        group = travelGroupRepository.saveAndFlush(group);

        GroupMemberEntity owner = new GroupMemberEntity();
        owner.setGroupId(group.getGroupId());
        owner.setUserId(ownerId);
        owner.setRole(GroupRole.OWNER);
        owner.setJoinedAt(LocalDateTime.now());
        groupMemberRepository.saveAndFlush(owner);
        return group;
    }

    private CreatePlannerRequestDto createRequest(
            String city,
            LocalDate startDate,
            LocalDate endDate
    ) {
        CreatePlannerRequestDto request = new CreatePlannerRequestDto();
        request.setTitle(city + " 여행");
        request.setMemberCount(1);
        request.setIsSolo(true);
        request.setCountryName(city);
        request.setCityName(city);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        return request;
    }

    private SavePlannerTravelPlanRequestDto saveRequest(
            String city,
            LocalDate startDate,
            LocalDate endDate
    ) {
        SavePlannerTravelPlanRequestDto request = new SavePlannerTravelPlanRequestDto();
        request.setCountryName(city);
        request.setCityName(city);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        return request;
    }

    private UserEntity user(String userId) {
        UserEntity user = new UserEntity();
        user.setUserId(userId);
        user.setUserPwd("password");
        user.setUserMail(userId + "@example.com");
        user.setNickName(userId);
        return user;
    }

    private record JoinResult(boolean success, String message) {
    }
}
