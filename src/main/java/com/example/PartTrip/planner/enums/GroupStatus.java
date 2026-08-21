package com.example.PartTrip.planner.enums;

// 여행 그룹 진행 상태 (앱 C7 의 "투표 진행 중" 같은 표시에 사용)
public enum GroupStatus {

    PLANNING,    // 그룹만 만들어진 상태
    VOTING,      // 카테고리별 투표 진행 중
    CONFIRMED,   // 투표가 끝나고 일정이 확정됨
    TRAVELING,   // 여행 중
    DONE         // 여행 종료
}
