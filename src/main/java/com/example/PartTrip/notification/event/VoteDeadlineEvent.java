package com.example.PartTrip.notification.event;

/**
 * 투표 마감이 임박했을 때. → {@code NotificationType.VOTE_DEADLINE}
 *
 * <p>발행: 알림 도메인의 스케줄러. 사용자 행동이 아니라 시간으로 발생하므로
 * 다른 도메인에서 발행할 일이 없다. {@code VoteRepository.findByStatusAndDeadlineBetween(...)}
 * 로 마감이 가까운 투표를 찾아 발행한다.
 * <p>수신: 아직 투표하지 않은 그룹 멤버들
 *
 * @param voteId  마감이 임박한 투표
 */
public record VoteDeadlineEvent(Long voteId) {}
