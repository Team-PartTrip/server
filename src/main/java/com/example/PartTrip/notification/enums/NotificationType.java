package com.example.PartTrip.notification.enums;

import lombok.Getter;

// 알림 종류
//
// @Deprecated 가 붙은 값은 발행하는 코드가 없다. 기능이 빠졌는데도 지우지 않은 이유는
// EnumType.STRING 이라 예전에 저장된 행을 읽을 때 enum 에 값이 없으면 조회가 터지기
// 때문이다. 지우려면 그 행을 먼저 지워야 한다.
//
// 실제로 발행되는 것은 네 가지다 (#143 에서 세 가지로 줄이는 것을 논의 중).
//   GROUP_INVITED · GROUP_INVITE_ACCEPTED  PlannerInvitationService · PlannerMemberService
//   TRIP_CARD_CREATED · REGION_VISITED     PlannerConfirmService
@Getter
public enum NotificationType {

    // 투표를 없앴다(#161). 이미 저장된 알림이 EnumType.STRING 으로 읽히도록 남긴다.
    @Deprecated
    VOTE_PARTICIPATED("투표 참여", NotificationCategory.VOTE),
    @Deprecated
    VOTE_DEADLINE("투표 마감 임박", NotificationCategory.VOTE),
    @Deprecated
    VOTE_REMINDER("투표 참여 요청", NotificationCategory.VOTE),
    // 투표가 빠진 뒤 VOTE 탭에 남은 알림은 이 둘뿐이다. 탭 이름을 바꿀지는 #143 에서 정한다
    GROUP_INVITED("그룹 초대", NotificationCategory.VOTE),
    GROUP_INVITE_ACCEPTED("그룹 초대 수락", NotificationCategory.VOTE),
    // 사진 정리 완료를 알리는 코드가 없다.
    @Deprecated
    PHOTO_ORGANIZED("사진 정리 완료", NotificationCategory.RECORD),
    // 세계지도를 대한민국 지도로 바꿨다(#162). 이미 저장된 알림이 읽히도록 남긴다.
    @Deprecated
    COUNTRY_ACQUIRED("국가 획득", NotificationCategory.RECORD),
    REGION_VISITED("새 지역 방문", NotificationCategory.RECORD),
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
