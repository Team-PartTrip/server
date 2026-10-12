package com.example.PartTrip.profile.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** AI 가 맛집을 고를 때 피하는 음식. 알러지 · 질환 같은 건강 정보는 받지 않는다 */
@Getter
@RequiredArgsConstructor
public enum AvoidFood {
    SEAFOOD("해산물"),
    SPICY("매운 음식"),
    HARD("딱딱하거나 질긴 음식"),
    MEAT("고기"),
    DAIRY("유제품");

    private final String label;
}
