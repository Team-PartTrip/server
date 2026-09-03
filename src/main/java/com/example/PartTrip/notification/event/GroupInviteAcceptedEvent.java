package com.example.PartTrip.notification.event;

/**
 * 초대받은 사람이 그룹에 참여했을 때. → {@code NotificationType.GROUP_INVITE_ACCEPTED}
 *
 * <p>발행: 플래너 — {@code POST /api/groups/join} 에서 {@code group_member} 저장 직후
 * <p>수신: 그룹장({@code GroupRole.OWNER})과 기존 멤버들
 *
 * @param groupId      참여가 일어난 그룹
 * @param actorUserId  새로 들어온 사람
 */
public record GroupInviteAcceptedEvent(Long groupId, String actorUserId) {}
