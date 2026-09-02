package com.example.PartTrip.notification.enums;

import lombok.Getter;

// 알림 종류 (Func-004-01 비고에 명시된 6가지)
@Getter
public enum NotificationType {

    VOTE_PARTICIPATED("투표 참여", NotificationCategory.VOTE),
    VOTE_DEADLINE("투표 마감 임박", NotificationCategory.VOTE),
    VOTE_REMINDER("투표 참여 요청", NotificationCategory.VOTE),
    GROUP_INVITED("그룹 초대", NotificationCategory.VOTE),
    GROUP_INVITE_ACCEPTED("그룹 초대 수락", NotificationCategory.VOTE),
    COUNTRY_ACQUIRED("국가 획득", NotificationCategory.RECORD),
    TRIP_CARD_CREATED("여행카드 생성", NotificationCategory.RECORD);

    // 설정 화면 토글 이름. 서버가 내려주면 앱이 한글 문구를 따로 들고 있지 않아도 된다.
    private final String label;

    // 종류마다 탭이 정해져 있다. 여기 묶어두면 알림을 만들 때 짝을 잘못 맞출 수 없다.
    private final NotificationCategory category;

    NotificationType(String label, NotificationCategory category) {
        this.label = label;
        this.category = category;
    }
}
