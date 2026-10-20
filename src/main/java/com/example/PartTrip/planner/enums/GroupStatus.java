package com.example.PartTrip.planner.enums;

// 여행 그룹 진행 상태
public enum GroupStatus {

    PLANNING,    // 그룹만 만들어진 상태
    /**
     * @deprecated 투표를 없앴다(#161). 더는 이 상태로 바꾸지 않는다.
     * db/remove_vote.sql 이 남은 행을 PLANNING 으로 돌린다.
     */
    @Deprecated
    VOTING,
    CONFIRMED,   // 일정이 확정됨
    TRAVELING,   // 여행 중
    DONE         // 여행 종료
}
