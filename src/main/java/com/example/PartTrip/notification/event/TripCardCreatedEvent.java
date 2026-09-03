package com.example.PartTrip.notification.event;

/**
 * 여행 카드가 만들어졌을 때. → {@code NotificationType.TRIP_CARD_CREATED}
 *
 * <p>발행: 여행 카드 — {@code POST /api/trip-cards} 저장 직후
 * <p>수신: 카드를 만든 본인
 *
 * @param tripCardId   만들어진 카드
 * @param actorUserId  카드를 만든 사람
 */
public record TripCardCreatedEvent(Long tripCardId, String actorUserId) {}
