package com.example.PartTrip.notification.listener;

import com.example.PartTrip.notification.enums.NotificationType;
import com.example.PartTrip.notification.event.GroupInviteAcceptedEvent;
import com.example.PartTrip.notification.event.GroupInvitedEvent;
import com.example.PartTrip.notification.event.VoteDeadlineEvent;
import com.example.PartTrip.notification.event.VoteParticipatedEvent;
import com.example.PartTrip.notification.event.VoteReminderEvent;
import com.example.PartTrip.notification.service.NotificationWriter;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.entity.VoteRecordEntity;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.planner.repository.VoteRecordRepository;
import com.example.PartTrip.planner.repository.VoteRepository;
import com.example.PartTrip.signup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// 플래너에서 올라온 이벤트를 알림으로 바꾼다 (NotificationCategory.VOTE)
//
// AFTER_COMMIT 이라 원래 작업이 롤백되면 알림도 생기지 않는다. 반대로 여기서 실패해도
// 원래 작업은 이미 커밋된 뒤라 되돌아가지 않는다. 그래서 예외를 밖으로 던지지 않고
// 로그만 남긴다. 알림이 하나 빠지는 것이 투표가 실패하는 것보다 낫다.
@Slf4j
@Component
@RequiredArgsConstructor
public class PlannerNotificationListener {

    private final NotificationWriter notificationWriter;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final VoteRecordRepository voteRecordRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final TravelGroupRepository travelGroupRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(VoteParticipatedEvent event) {

        try {
            VoteEntity vote = voteRepository.findById(event.voteId()).orElse(null);
            if (vote == null) {
                return;
            }

            // 투표한 본인은 빼고 같은 그룹의 나머지 멤버에게 알린다
            List<String> recipients = membersOfPlan(vote.getPlanId()).stream()
                    .filter(userId -> !userId.equals(event.actorUserId()))
                    .toList();

            notificationWriter.writeAll(
                    recipients,
                    NotificationType.VOTE_PARTICIPATED,
                    NotificationType.VOTE_PARTICIPATED.getLabel(),
                    nickNameOf(event.actorUserId()) + "님이 "
                            + vote.getCategory().getLabel() + " 투표에 참여했어요.",
                    "VOTE",
                    vote.getVoteId());

        } catch (Exception e) {
            log.warn("투표 참여 알림 생성 실패 voteId={}", event.voteId(), e);
        }
    }

    // 스케줄러가 트랜잭션 없이 발행하므로 fallbackExecution 이 필요하다.
    // 이 값이 없으면 트랜잭션 밖에서 발행된 이벤트는 조용히 버려진다.
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void on(VoteDeadlineEvent event) {

        try {
            VoteEntity vote = voteRepository.findById(event.voteId()).orElse(null);
            if (vote == null) {
                return;
            }

            // 이미 투표한 사람에게는 마감을 알릴 이유가 없다
            Set<String> voted = voteRecordRepository.findByVoteId(event.voteId()).stream()
                    .map(VoteRecordEntity::getUserId)
                    .collect(Collectors.toSet());

            List<String> recipients = membersOfPlan(vote.getPlanId()).stream()
                    .filter(userId -> !voted.contains(userId))
                    .toList();

            notificationWriter.writeAll(
                    recipients,
                    NotificationType.VOTE_DEADLINE,
                    NotificationType.VOTE_DEADLINE.getLabel(),
                    vote.getCategory().getLabel() + " 투표가 곧 마감돼요. 아직 투표하지 않았어요.",
                    "VOTE",
                    vote.getVoteId());

        } catch (Exception e) {
            log.warn("투표 마감 임박 알림 생성 실패 voteId={}", event.voteId(), e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(GroupInviteAcceptedEvent event) {

        try {
            TravelGroupEntity group = travelGroupRepository.findById(event.groupId()).orElse(null);
            if (group == null) {
                return;
            }

            // 새로 들어온 본인은 빼고 기존 멤버에게 알린다
            List<String> recipients = membersOfGroup(event.groupId()).stream()
                    .filter(userId -> !userId.equals(event.actorUserId()))
                    .toList();

            String groupName = group.getGroupName() == null ? "여행 그룹" : group.getGroupName();

            notificationWriter.writeAll(
                    recipients,
                    NotificationType.GROUP_INVITE_ACCEPTED,
                    NotificationType.GROUP_INVITE_ACCEPTED.getLabel(),
                    nickNameOf(event.actorUserId()) + "님이 " + groupName + "에 참여했어요.",
                    "GROUP",
                    group.getGroupId());

        } catch (Exception e) {
            log.warn("그룹 참여 알림 생성 실패 groupId={}", event.groupId(), e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(GroupInvitedEvent event) {
        try {
            TravelGroupEntity group = travelGroupRepository.findById(event.groupId()).orElse(null);
            if (group == null) {
                return;
            }
            String groupName = group.getGroupName() == null ? "여행 그룹" : group.getGroupName();
            notificationWriter.write(
                    event.invitedUserId(),
                    NotificationType.GROUP_INVITED,
                    NotificationType.GROUP_INVITED.getLabel(),
                    nickNameOf(event.actorUserId()) + "님이 " + groupName + "에 초대했어요.",
                    "GROUP_INVITATION",
                    group.getGroupId());
        } catch (Exception e) {
            log.warn("그룹 초대 알림 생성 실패 groupId={}", event.groupId(), e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(VoteReminderEvent event) {
        try {
            GroupTravelPlanEntity plan = groupTravelPlanRepository
                    .findFirstByGroupIdOrderByCreatedAtDesc(event.groupId())
                    .orElse(null);
            if (plan == null) {
                return;
            }
            List<VoteEntity> activeVotes = voteRepository.findByPlanId(plan.getPlanId()).stream()
                    .filter(vote -> vote.getStatus() == com.example.PartTrip.planner.enums.VoteStatus.OPEN)
                    .toList();
            if (activeVotes.isEmpty()) {
                return;
            }
            Set<String> completedAll = membersOfGroup(event.groupId()).stream()
                    .filter(userId -> activeVotes.stream().allMatch(vote ->
                            voteRecordRepository.findByVoteIdAndUserId(vote.getVoteId(), userId).isPresent()))
                    .collect(Collectors.toSet());
            List<String> recipients = membersOfGroup(event.groupId()).stream()
                    .filter(userId -> !userId.equals(event.actorUserId()))
                    .filter(userId -> !completedAll.contains(userId))
                    .toList();
            notificationWriter.writeAll(
                    recipients,
                    NotificationType.VOTE_REMINDER,
                    NotificationType.VOTE_REMINDER.getLabel(),
                    nickNameOf(event.actorUserId()) + "님이 아직 남은 투표 참여를 요청했어요.",
                    "GROUP",
                    event.groupId());
        } catch (Exception e) {
            log.warn("투표 재촉 알림 생성 실패 groupId={}", event.groupId(), e);
        }
    }

    private List<String> membersOfPlan(Long planId) {

        return groupTravelPlanRepository.findById(planId)
                .map(GroupTravelPlanEntity::getGroupId)
                .map(this::membersOfGroup)
                .orElseGet(List::of);
    }

    private List<String> membersOfGroup(Long groupId) {

        return groupMemberRepository.findByGroupIdOrderByJoinedAtAsc(groupId).stream()
                .map(member -> member.getUserId())
                .toList();
    }

    private String nickNameOf(String userId) {

        return userRepository.findByUserId(userId)
                .map(user -> user.getNickName())
                .orElse("알 수 없는 사용자");
    }
}
