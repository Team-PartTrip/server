/**
 * 다른 도메인이 알림을 발생시킬 때 쓰는 이벤트 모음.
 *
 * <p>알림은 플래너 · 여행 카드 · 기록에서 일어난 일을 재료로 만들어진다.
 * 그렇다고 각 도메인이 {@code NotificationRepository} 를 직접 부르면
 * 알림 저장 방식이 바뀔 때마다 세 도메인을 모두 고쳐야 한다.
 * 그래서 각 도메인은 "무슨 일이 일어났는지"만 알리고, 알림을 만드는 일은 알림 도메인이 맡는다.
 *
 * <h2>발행하는 쪽 (플래너 · 여행 카드 · 기록)</h2>
 *
 * <pre>{@code
 * private final ApplicationEventPublisher eventPublisher;
 *
 * @Transactional
 * public void vote(Long voteId, String userId) {
 *     voteRecordRepository.save(...);
 *     eventPublisher.publishEvent(new VoteParticipatedEvent(voteId, userId));
 * }
 * }</pre>
 *
 * 이 한 줄이 전부다. 알림이 실제로 어떻게 저장되는지는 몰라도 된다.
 *
 * <h2>받는 쪽 (알림)</h2>
 *
 * <pre>{@code
 * @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
 * public void on(VoteParticipatedEvent event) { ... }
 * }</pre>
 *
 * {@code AFTER_COMMIT} 이라 원래 작업이 롤백되면 알림도 만들어지지 않는다.
 * 반대로 알림 저장이 실패해도 원래 작업은 이미 커밋된 뒤라 되돌아가지 않는다.
 *
 * <h2>이벤트에 무엇을 담는가</h2>
 *
 * 식별자만 담는다. 알림 문구에 필요한 이름 · 제목 등은 알림 도메인이 조회해서 채운다.
 * 이벤트에 문구를 담으면 알림 형식이 바뀔 때마다 발행하는 쪽을 고쳐야 한다.
 *
 * <p>사용자 행동으로 생기는 이벤트는 <b>행동한 사람</b>({@code actorUserId})을 담는다.
 * 알림을 <b>받을</b> 사람이 아니다. 받는 사람은 알림 도메인이 정한다 —
 * 투표 참여는 같은 그룹의 다른 멤버들이 받고, 여행 카드 생성은 만든 본인이 받는 식이다.
 */
package com.example.PartTrip.notification.event;
