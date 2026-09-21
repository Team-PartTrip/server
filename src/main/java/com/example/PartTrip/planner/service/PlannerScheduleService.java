package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.planner.dto.response.ConfirmedPlaceResponseDto;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.PlannerCityEntity;
import com.example.PartTrip.planner.entity.PlannerScheduleSlotEntity;
import com.example.PartTrip.planner.repository.PlannerCityRepository;
import com.example.PartTrip.profile.service.TravelPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/** 확정 장소를 날짜별 동선으로 바꾸는 일정 생성기 (Func-011-01). */
@Service
@RequiredArgsConstructor
public class PlannerScheduleService {

    private final PlannerCityRepository plannerCityRepository;
    private final TourPlaceRepository tourPlaceRepository;
    private final TravelPreferenceService travelPreferenceService;

    /** 사용자의 하루 일정 개수를 적용해 확정 장소를 날짜별로 배치한다. */
    @Transactional(readOnly = true)
    public List<ScheduledPlace> buildSchedule(
            GroupTravelPlanEntity plan,
            List<ConfirmedPlaceResponseDto> confirmedPlaces,
            String userId
    ) {
        int dailyScheduleCount = travelPreferenceService
                .getPreference(userId)
                .getDailyScheduleCount();
        List<PlannerCityEntity> cities = plannerCityRepository
                .findByPlanIdOrderBySeqAsc(plan.getPlanId());
        if (cities.isEmpty()) {
            cities = List.of(singleCity(plan));
        }

        Set<Long> confirmedIds = confirmedPlaces.stream()
                .map(ConfirmedPlaceResponseDto::getTourPlaceId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());
        Map<Long, TourPlaceEntity> placesById = tourPlaceRepository.findAllById(confirmedIds)
                .stream().collect(Collectors.toMap(TourPlaceEntity::getTourPlaceId, Function.identity()));

        List<ScheduledPlace> result = new ArrayList<>();
        Set<Long> alreadyScheduled = new HashSet<>();
        for (PlannerCityEntity city : cities) {
            List<TourPlaceEntity> selected = confirmedPlaces.stream()
                    .map(place -> placesById.get(place.getTourPlaceId()))
                    .filter(place -> place != null
                            && sameCity(place, city)
                            && (place.getCategory() == TourPlaceCategory.ACCOMMODATION
                            || !alreadyScheduled.contains(place.getTourPlaceId())))
                    .toList();
            selected.forEach(place -> alreadyScheduled.add(place.getTourPlaceId()));
            result.addAll(scheduleCity(
                    city, selected, alreadyScheduled, dailyScheduleCount));
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<ScheduledPlace> fromSlots(List<PlannerScheduleSlotEntity> slots) {
        Map<Long, TourPlaceEntity> placesById = tourPlaceRepository.findAllById(slots.stream()
                        .map(PlannerScheduleSlotEntity::getTourPlaceId)
                        .filter(id -> id != null)
                        .collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(TourPlaceEntity::getTourPlaceId, Function.identity()));
        List<ScheduledPlace> result = new ArrayList<>();
        for (PlannerScheduleSlotEntity slot : slots) {
            TourPlaceEntity place = placesById.get(slot.getTourPlaceId());
            if (place != null) {
                result.add(new ScheduledPlace(
                        toResponse(place), place, slot.getVisitDate(), slot.getSortOrder()));
            }
        }
        return result;
    }

    /** 한 도시의 확정 장소와 추천 장소를 날짜별 일정으로 구성한다. */
    private List<ScheduledPlace> scheduleCity(
            PlannerCityEntity city,
            List<TourPlaceEntity> confirmed,
            Set<Long> alreadyScheduled,
            int dailyScheduleCount
    ) {
        int days = validDays(city.getStartDate(), city.getEndDate());
        List<TourPlaceEntity> accommodations = confirmed.stream()
                .filter(place -> place.getCategory() == TourPlaceCategory.ACCOMMODATION)
                .sorted(ratingOrder())
                .toList();
        List<TourPlaceEntity> dayPlaces = new ArrayList<>(confirmed.stream()
                .filter(place -> place.getCategory() != TourPlaceCategory.ACCOMMODATION)
                .toList());

        int targetCount = days * dailyScheduleCount;
        if (dayPlaces.size() < targetCount) {
            List<TourPlaceEntity> recommendations = tourPlaceRepository
                    .search(city.getCountryName(), city.getCityName(), null);
            for (TourPlaceEntity recommendation : recommendations) {
                if (dayPlaces.size() >= targetCount) break;
                if (recommendation.getCategory() == TourPlaceCategory.ACCOMMODATION) continue;
                if (alreadyScheduled.add(recommendation.getTourPlaceId())) {
                    dayPlaces.add(recommendation);
                }
            }
        }

        List<TourPlaceEntity> route = nearestNeighbourRoute(dayPlaces);
        List<List<TourPlaceEntity>> byDay = splitEvenly(route, days);
        List<ScheduledPlace> scheduled = new ArrayList<>();
        for (int day = 0; day < days; day++) {
            LocalDate date = city.getStartDate().plusDays(day);
            int order = 1;
            List<TourPlaceEntity> places = new ArrayList<>(byDay.get(day));
            places.sort(ratingOrder());
            for (TourPlaceEntity place : places) {
                scheduled.add(new ScheduledPlace(toResponse(place), place, date, order++));
            }
            if (!accommodations.isEmpty()) {
                // 숙소는 이동 동선의 기준점이므로 도시 체류 중 매일 같은 곳을 쓴다.
                TourPlaceEntity accommodation = accommodations.get(0);
                scheduled.add(new ScheduledPlace(
                        toResponse(accommodation), accommodation, date, order));
            }
        }
        return scheduled;
    }

    /** 좌표가 있는 장소들을 가까운 순서로 이어 이동 경로를 만든다. */
    private List<TourPlaceEntity> nearestNeighbourRoute(List<TourPlaceEntity> places) {
        if (places.size() < 2) return new ArrayList<>(places);
        List<TourPlaceEntity> remaining = new ArrayList<>(places);
        remaining.sort(ratingOrder());
        List<TourPlaceEntity> route = new ArrayList<>();
        TourPlaceEntity current = remaining.remove(0);
        route.add(current);
        while (!remaining.isEmpty()) {
            TourPlaceEntity from = current;
            current = remaining.stream()
                    .min(Comparator.comparingDouble(place -> distance(from, place)))
                    .orElseThrow();
            remaining.remove(current);
            route.add(current);
        }
        return route;
    }

    /** 전체 이동 경로를 여행 일수에 맞춰 균등하게 나눈다. */
    private List<List<TourPlaceEntity>> splitEvenly(List<TourPlaceEntity> route, int days) {
        List<List<TourPlaceEntity>> result = new ArrayList<>();
        int cursor = 0;
        for (int day = 0; day < days; day++) {
            int remainingPlaces = route.size() - cursor;
            int remainingDays = days - day;
            int count = remainingPlaces == 0 ? 0
                    : (int) Math.ceil((double) remainingPlaces / remainingDays);
            result.add(new ArrayList<>(route.subList(cursor, cursor + count)));
            cursor += count;
        }
        return result;
    }

    /** 두 장소의 좌표로 대권 거리를 계산한다. */
    private double distance(TourPlaceEntity first, TourPlaceEntity second) {
        if (first.getLatitude() == null || first.getLongitude() == null
                || second.getLatitude() == null || second.getLongitude() == null) {
            return Double.MAX_VALUE;
        }
        double lat1 = Math.toRadians(first.getLatitude());
        double lat2 = Math.toRadians(second.getLatitude());
        double dLat = lat2 - lat1;
        double dLon = Math.toRadians(second.getLongitude() - first.getLongitude());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 6_371.0 * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    /** 평점 내림차순과 장소명 오름차순으로 정렬하는 비교기를 반환한다. */
    private Comparator<TourPlaceEntity> ratingOrder() {
        return Comparator.comparing(
                        TourPlaceEntity::getRating,
                        Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(TourPlaceEntity::getPlaceName,
                        Comparator.nullsLast(String::compareTo));
    }

    /** 시작일과 종료일을 포함한 유효 여행 일수를 계산한다. */
    private int validDays(LocalDate start, LocalDate end) {
        int days = Math.toIntExact(ChronoUnit.DAYS.between(start, end) + 1);
        if (days <= 0) throw new IllegalArgumentException("여행 기간이 올바르지 않습니다.");
        return days;
    }

    /** 장소가 일정에 지정된 국가와 도시에 속하는지 확인한다. */
    private boolean sameCity(TourPlaceEntity place, PlannerCityEntity city) {
        return city.getCountryName().equals(place.getCountryName())
                && city.getCityName().equals(place.getCityName());
    }

    /** 도시별 일정이 없을 때 기존 여행 계획을 단일 도시 일정으로 변환한다. */
    private PlannerCityEntity singleCity(GroupTravelPlanEntity plan) {
        PlannerCityEntity city = new PlannerCityEntity();
        city.setPlanId(plan.getPlanId());
        city.setSeq(0);
        city.setCountryName(plan.getCountryName());
        city.setCityName(plan.getCityName());
        city.setStartDate(plan.getStartDate());
        city.setEndDate(plan.getEndDate());
        return city;
    }

    /** 장소 엔티티를 확정 장소 응답으로 변환한다. */
    private ConfirmedPlaceResponseDto toResponse(TourPlaceEntity place) {
        return ConfirmedPlaceResponseDto.builder()
                .category(place.getCategory() == null ? null : place.getCategory().name())
                .tourPlaceId(place.getTourPlaceId())
                .placeName(place.getPlaceName())
                .address(place.getAddress())
                .rating(place.getRating())
                .build();
    }

    /** 날짜와 노출 순서가 지정된 일정 장소이다. */
    public record ScheduledPlace(
            ConfirmedPlaceResponseDto place,
            TourPlaceEntity tourPlace,
            LocalDate date,
            int sortOrder
    ) {}
}
