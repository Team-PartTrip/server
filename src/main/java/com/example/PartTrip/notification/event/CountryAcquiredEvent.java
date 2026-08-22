package com.example.PartTrip.notification.event;

/**
 * 사용자가 새로운 국가를 처음 방문했을 때. → {@code NotificationType.COUNTRY_ACQUIRED}
 *
 * <p>발행: 기록 · 세계지도 — {@code visited_country} 에 <b>행이 새로 만들어졌을 때만</b>.
 * 이미 다녀온 나라를 다시 방문해 {@code visitCount} 만 올라간 경우에는 발행하지 않는다.
 * {@code VisitedCountryRepository.existsByUserIdAndCountryInfoId(...)} 로 구분한다.
 * <p>수신: 방문한 본인
 *
 * @param countryInfoId  획득한 국가 ({@code country_info} FK)
 * @param actorUserId    방문한 사람
 */
public record CountryAcquiredEvent(Long countryInfoId, String actorUserId) {}
