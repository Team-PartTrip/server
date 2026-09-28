package com.example.PartTrip.planner.dto.response;

import java.time.LocalDate;
import java.util.List;

/** 날짜별 일정 카드 (#158 · #131). 장소를 못 정한 칸은 place 가 null 이다 */
public record PlannerScheduleResponseDto(
        Long plannerId,
        String title,
        String cityName,
        LocalDate startDate,
        LocalDate endDate,
        List<Day> days
) {
    public record Day(LocalDate date, List<Slot> slots) {}

    public record Slot(Long slotId, int order, Place place, String routeStatus, RouteLeg routeFromPrevious) {
        public Slot(Long slotId, int order, Place place) {
            this(slotId, order, place, null, null);
        }
    }

    public record RouteLeg(
            String transportMode,
            String fromName,
            String toName,
            Integer durationMinutes,
            Integer walkingMinutes,
            List<RouteStep> steps
    ) {}

    public record RouteStep(
            String type,
            String name,
            String boardingStop,
            String alightingStop,
            Integer stopCount,
            Integer durationMinutes
    ) {}

    public record Place(
            Long tourPlaceId,
            String name,
            String category,
            String categoryLabel,
            String imageUrl,
            String address,
            Double rating,
            Double latitude,
            Double longitude
    ) {}
}
