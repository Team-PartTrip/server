package com.example.PartTrip.main.dto;

import java.util.List;

public record AccessibilityResponseDto(boolean matched, Status status, List<Item> items) {

    public enum Status { FOUND, NOT_FOUND, UNAVAILABLE }

    public static AccessibilityResponseDto found(List<Item> items) {
        return new AccessibilityResponseDto(true, items.isEmpty() ? Status.NOT_FOUND : Status.FOUND, items);
    }

    public static AccessibilityResponseDto notFound() {
        return new AccessibilityResponseDto(false, Status.NOT_FOUND, List.of());
    }

    public static AccessibilityResponseDto unavailable() {
        return new AccessibilityResponseDto(false, Status.UNAVAILABLE, List.of());
    }

    public record Item(String key, String label, String text) {
    }
}
