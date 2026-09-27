package com.example.PartTrip.notification.event;

/**
 * 여행 카드가 만들어졌을 때. → {@code NotificationType.TRIP_CARD_CREATED}
 *
 * <p>발행: 플래너 — {@code POST /api/planners/{plannerId}/confirm} 에서 카드 저장 직후
 *    ({@code PlannerConfirmService}). 여행 카드 쪽에는 카드를 만드는 API 가 없다
 * <p>수신: 카드를 만든 본인
 *
 * @param tripCardId   만들어진 카드
 * @param actorUserId  카드를 만든 사람
 */
public record TripCardCreatedEvent(Long tripCardId, String actorUserId) {}
