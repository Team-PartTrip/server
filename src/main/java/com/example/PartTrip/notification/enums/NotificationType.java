package com.example.PartTrip.notification.enums;

import lombok.Getter;

// 알림 종류 (Func-004-01 비고에 명시된 6가지)
@Getter
public enum NotificationType {

    VOTE_PARTICIPATED("투표 참여"),
    VOTE_DEADLINE("투표 마감 임박"),
    PHOTO_ORGANIZED("사진 정리 완료"),
    COUNTRY_ACQUIRED("국가 획득"),
    TRIP_CARD_CREATED("여행카드 생성"),
    GROUP_INVITE_ACCEPTED("그룹 초대 수락");

    // 설정 화면 토글 이름. 서버가 내려주면 앱이 한글 문구를 따로 들고 있지 않아도 된다.
    private final String label;

    NotificationType(String label) {
        this.label = label;
    }
}
