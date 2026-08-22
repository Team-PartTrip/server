package com.example.PartTrip.notification.event;

/**
 * 해설 카메라 사진 분석이 끝났을 때. → {@code NotificationType.PHOTO_ORGANIZED}
 *
 * <p>발행: 기록 — 사진 분석 결과({@code photo_analysis})를 저장한 직후
 * <p>수신: 사진을 올린 본인
 *
 * <p>분석은 외부 API 호출이라 시간이 걸린다. 사용자가 앱을 닫아도 끝나면 알려주기 위한 알림이다.
 *
 * @param photoId      분석이 끝난 사진
 * @param actorUserId  사진을 올린 사람
 */
public record PhotoOrganizedEvent(Long photoId, String actorUserId) {}
