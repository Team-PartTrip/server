package com.example.PartTrip.notification.event;

/**
 * 사용자가 처음 가 보는 시·도의 여행카드가 생겼을 때. → {@code NotificationType.REGION_VISITED}
 *
 * 국가 획득(CountryAcquiredEvent)을 대신한다. 국가는 여행이 끝난 뒤에 땄지만
 * 지역은 카드가 생기는 순간 기록된다(#162). 갔다 와야 인정하던 걸 가기로
 * 정하면 인정하는 쪽으로 바꾼 것이라, 알림도 그만큼 앞당겨진다.
 */
public record RegionVisitedEvent(String regionCode, String actorUserId) {}
