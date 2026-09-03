package com.example.PartTrip.notification.event;

/**
 * 그룹원이 투표에 참여했을 때. → {@code NotificationType.VOTE_PARTICIPATED}
 *
 * <p>발행: 플래너 — {@code POST /api/votes/{voteId}/records} 저장 직후
 * <p>수신: 같은 그룹의 <b>다른</b> 멤버들 (행동한 본인 제외)
 *
 * @param voteId       참여한 투표
 * @param actorUserId  투표한 사람. 알림을 받을 사람이 아니라 행동한 사람이다.
 */
public record VoteParticipatedEvent(Long voteId, String actorUserId) {}
