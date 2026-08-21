package com.example.PartTrip.planner.enums;

// 카테고리별 투표 상태
public enum VoteStatus {

    OPEN,        // 투표 진행 중
    CLOSED,      // 마감됨 (아직 확정 전)
    CONFIRMED    // 최다 득표 장소가 확정됨
}
