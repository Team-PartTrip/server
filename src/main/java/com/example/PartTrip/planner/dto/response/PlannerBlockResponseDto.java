package com.example.PartTrip.planner.dto.response;

import com.example.PartTrip.planner.enums.PlannerBlockType;

import java.util.List;

public record PlannerBlockResponseDto(
        String type,
        String label,
        boolean multiple,
        List<String> options
) {
    public static PlannerBlockResponseDto from(PlannerBlockType type) {
        return new PlannerBlockResponseDto(
                type.name(), type.getLabel(), type.isMultiple(), type.getOptions());
    }
}
