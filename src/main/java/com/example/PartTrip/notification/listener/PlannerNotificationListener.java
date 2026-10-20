package com.example.PartTrip.notification.listener;

import com.example.PartTrip.notification.enums.NotificationType;
import com.example.PartTrip.notification.event.GroupInviteAcceptedEvent;
import com.example.PartTrip.notification.event.GroupInvitedEvent;
import com.example.PartTrip.notification.service.NotificationWriter;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.signup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

// 플래너에서 올라온 이벤트를 알림으로 바꾼다 (NotificationCategory.VOTE)
//
// AFTER_COMMIT 이라 원래 작업이 롤백되면 알림도 생기지 않는다. 반대로 여기서 실패해도
// 원래 작업은 이미 커밋된 뒤라 되돌아가지 않는다. 그래서 예외를 밖으로 던지지 않고
// 로그만 남긴다. 알림이 하나 빠지는 것이 초대가 실패하는 것보다 낫다.
@Slf4j
@Component
@RequiredArgsConstructor
public class PlannerNotificationListener {

    private final NotificationWriter notificationWriter;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final TravelGroupRepository travelGroupRepository;

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
