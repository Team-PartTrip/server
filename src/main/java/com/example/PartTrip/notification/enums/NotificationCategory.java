package com.example.PartTrip.notification.enums;

// 알림 목록의 탭 구분 (Func-004-01: "전체 / 투표 / 기록 탭으로 구분")
// 전체는 필터 없음이므로 값이 필요 없다.
//
// 투표가 빠졌지만(#161) VOTE 라는 이름은 그대로 둔다. 앱이
// GET /api/notifications?type=VOTE 로 보내는 값이라 서버 혼자 바꾸면 탭이 빈다.
// 바꿀지는 #143 에서 앱과 함께 정한다.
public enum NotificationCategory {

    VOTE,     // 플래너 관련 — 지금은 그룹 초대 · 초대 수락
    RECORD    // 기록 관련 — 여행카드 생성 · 새 지역 방문
}
