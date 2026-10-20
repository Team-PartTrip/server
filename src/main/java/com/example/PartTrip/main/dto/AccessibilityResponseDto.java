package com.example.PartTrip.main.dto;

import java.util.List;

public record AccessibilityResponseDto(boolean matched, List<Item> items) {

    public static AccessibilityResponseDto none() {
        return new AccessibilityResponseDto(false, List.of());
    }

    public record Item(String key, String label, String text) {
    }
}
