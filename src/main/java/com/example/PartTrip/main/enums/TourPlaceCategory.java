package com.example.PartTrip.main.enums;

import lombok.Getter;

import java.util.Arrays;

// 관광지 카테고리
@Getter
public enum TourPlaceCategory {

    RESTAURANT("맛집"),
    ATTRACTION("명소"),
    ACCOMMODATION("숙소"),
    CAFE("카페"),
    ACTIVITY("액티비티"),
    SHOPPING("쇼핑");

    // 앱에 보여줄 한글 이름
    private final String label;

    TourPlaceCategory(String label) {
        this.label = label;
    }

    // "RESTAURANT" 와 "맛집" 을 모두 받아준다
    public static TourPlaceCategory from(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return Arrays.stream(values())
                .filter(category -> category.name().equalsIgnoreCase(value.trim())
                        || category.label.equals(value.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "지원하지 않는 카테고리입니다. (" + labels() + " 중 하나를 사용해주세요.)"));
    }

    // 안내 메시지에 쓸 카테고리 목록
    public static String labels() {
        return Arrays.stream(values())
                .map(TourPlaceCategory::getLabel)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }
}
