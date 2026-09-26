package com.example.PartTrip.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 마이 탭 상단의 "여행 · 지역 · 기록" 3칸 (Func-007-01)
@Getter
@AllArgsConstructor
public class ProfileStatsResponseDto {

    // 내가 만든 여행 카드 수
    private long tripCount;

    // 다녀온 시·도 수
    private long regionCount;

    // 내 여행 카드에 담긴 사진 수
    private long recordCount;
}
