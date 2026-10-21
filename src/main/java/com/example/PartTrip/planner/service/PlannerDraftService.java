package com.example.PartTrip.planner.service;

import com.example.PartTrip.global.exception.AiUnavailableException;
import com.example.PartTrip.global.exception.ForbiddenException;
import com.example.PartTrip.global.exception.NotFoundException;
import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.main.entity.TourPlaceAccessibilityEntity;
import com.example.PartTrip.main.repository.TourPlaceAccessibilityRepository;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.main.service.TourPlaceImportService;
import com.example.PartTrip.main.service.TourPlaceRanking;
import com.example.PartTrip.planner.dto.request.CreatePlannerRequestDto;
import com.example.PartTrip.region.enums.RegionCode;
import com.example.PartTrip.planner.dto.request.GeneratePlannerRequestDto;
import com.example.PartTrip.planner.dto.response.PlannerScheduleResponseDto;
import com.example.PartTrip.planner.dto.response.PlannerSurveyResponseDto;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.PlannerScheduleSlotEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.enums.PlannerBlockType;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.PlannerScheduleSlotRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.profile.dto.TravelPreferenceResponseDto;
import com.example.PartTrip.profile.enums.AvoidFood;
import com.example.PartTrip.profile.service.TravelPreferenceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 블록 지침으로 AI 일정 초안을 만든다 (#158).
 *
 * 1. 그 도시의 관광지 후보를 모은다 (없으면 구글에서 받아온다)
 * 2. 후보 목록과 블록을 AI 에게 준다. AI 는 후보 id 로만 답한다
 * 3. 서버가 답을 검사한다. 후보에 없는 id 는 버리고 그 자리는 빈 카드로 둔다
 * 4. 플래너와 카드를 한 트랜잭션으로 저장한다
 *
 * AI 호출(수십 초)을 트랜잭션 밖에서 한다. 실패하면 플래너가 아예 생기지 않는다.
 */
@Service
@RequiredArgsConstructor
public class PlannerDraftService {

    static final String KOREA = "대한민국";
    static final int MAX_DAYS = 14;
    static final int MAX_PER_DAY = 6;
    /** 프롬프트가 너무 길어지지 않게 평점 순으로 자른다 */
    static final int MAX_CANDIDATES = 150;
    private static final long REGENERATION_COOLDOWN_SECONDS = 30;

    private static final Pattern NUMBER = Pattern.compile("\\d+");

    private final TourPlaceImportService tourPlaceImportService;
    private final TourPlaceRepository tourPlaceRepository;
    private final TourPlaceAccessibilityRepository accessibilityRepository;
    private final TravelPreferenceService travelPreferenceService;
    private final OpenAiClient openAiClient;
    private final PlannerService plannerService;
    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final PlannerScheduleSlotRepository slotRepository;
    private final TransactionTemplate transactionTemplate;
    private final ObjectMapper objectMapper;

    record DepartureDetails(String name, Double latitude, Double longitude) {
        boolean hasCoordinates() {
            return latitude != null && longitude != null;
        }
    }

    private record GeneratedSchedule(
            String city,
            List<LocalDate> dates,
            List<List<Long>> days,
            Long lodgingId,
            DepartureDetails departure
    ) {}

    public PlannerScheduleResponseDto generate(GeneratePlannerRequestDto dto, String userId) {
        if (dto.getBlocks() == null || !dto.isBlockSelectionValid()) {
            throw new IllegalArgumentException("블록 선택값이 올바르지 않습니다. 선택지와 입력 개수를 확인해주세요.");
        }
        datesOf(dto.getStartDate(), dto.getEndDate());
        // AI 를 부르기 전에 막는다. 저장할 때 걸리면 AI 비용만 나간다.
        // @NotBlank 는 "99" 같은 없는 코드를 거르지 못한다
        RegionCode.of(dto.getRegionCode());
        if (groupTravelPlanRepository.existsOverlappingPlanForUser(
                userId, dto.getStartDate(), dto.getEndDate())) {
            throw new IllegalArgumentException("해당 기간에 이미 등록된 여행 계획이 있습니다.");
        }

        TravelPreferenceResponseDto preference = travelPreferenceService.getPreference(userId);
        GeneratedSchedule generated = buildSchedule(dto, preference);

        Long plannerId = transactionTemplate.execute(status -> {
            Long id = plannerService
                    .createPlanner(toCreateRequest(dto, generated.city()), userId)
                    .getPlannerId();
            GroupTravelPlanEntity plan = latestPlan(id);
            if (generated.departure() != null) {
                plan.setDeparturePlaceName(generated.departure().name());
                plan.setDepartureLatitude(generated.departure().latitude());
                plan.setDepartureLongitude(generated.departure().longitude());
            }
            plan.setSurveyResponses(serializeSurvey(dto.getBlocks()));
            groupTravelPlanRepository.save(plan);
            slotRepository.saveAll(toEntities(plan.getPlanId(), generated.dates(),
                    generated.days(), generated.lodgingId()));
            return id;
        });
        return getSchedule(plannerId, userId);
    }

    private GeneratedSchedule buildSchedule(
            GeneratePlannerRequestDto dto, TravelPreferenceResponseDto preference) {
        String city = dto.getCityName().trim();
        List<LocalDate> dates = datesOf(dto.getStartDate(), dto.getEndDate());
        RegionCode.of(dto.getRegionCode());
        List<String> mustInclude = values(dto, PlannerBlockType.MUST_INCLUDE);
        List<String> exclude = values(dto, PlannerBlockType.EXCLUDE);

        tourPlaceImportService.importCityIfEmpty(KOREA, city);
        List<TourPlaceEntity> all = tourPlaceRepository
                .findByCountryNameAndCityName(KOREA, city).stream()
                .filter(place -> !matchesAny(place.getPlaceName(), exclude))
                .toList();
        Integer walkingLimit = walkingLimit(dto, preference);
        Set<Long> barrierFree = barrierFreeIds(preference, all, walkingLimit);
        boolean limitedMobility = Boolean.TRUE.equals(preference.getUsesWheelchair())
                || walkingLimit != null && walkingLimit < 60;
        List<TourPlaceEntity> must = all.stream()
                .filter(place -> place.getCategory() != TourPlaceCategory.ACCOMMODATION)
                .filter(place -> matchesAny(place.getPlaceName(), mustInclude))
                .toList();
        Double globalAverage = tourPlaceRepository.findAverageRating();
        List<TourPlaceEntity> daytime = Stream.concat(must.stream(), all.stream()
                        .filter(place -> place.getCategory() != TourPlaceCategory.ACCOMMODATION)
                        .filter(place -> !limitedMobility
                                || place.getCategory() != TourPlaceCategory.ACTIVITY
                                || barrierFree.contains(place.getTourPlaceId()))
                        .sorted(TourPlaceRanking.byBayesianRating(globalAverage)))
                .distinct()
                .limit(Math.max(MAX_CANDIDATES, must.size()))
                .toList();
        if (daytime.isEmpty()) {
            throw new IllegalArgumentException(city + "에서 일정에 넣을 장소를 찾지 못했어요.");
        }
        boolean needsLodging = dates.size() > 1
                && !values(dto, PlannerBlockType.LODGING_TYPE).contains("숙박 없음");
        List<TourPlaceEntity> lodgings = needsLodging
                ? all.stream().filter(place -> place.getCategory() == TourPlaceCategory.ACCOMMODATION).toList()
                : List.of();

        int perDay = slotsPerDay(last(values(dto, PlannerBlockType.DAILY_DENSITY)),
                preference.getDailyScheduleCount());
        if (must.size() > dates.size() * perDay) {
            throw new IllegalArgumentException("반드시 포함할 곳이 " + must.size()
                    + "곳인데 일정에는 " + dates.size() * perDay + "칸뿐이에요. 일정 밀도를 늘리거나 줄여주세요.");
        }

        DepartureDetails departure = departureDetails(dto, preference);
        JsonNode answer = parse(openAiClient.completeJson(
                SYSTEM_PROMPT,
                userPrompt(dto, city, dates, perDay, preference, daytime, lodgings,
                        barrierFree, departure, walkingLimit)));

        Set<Long> daytimeIds = daytime.stream()
                .map(TourPlaceEntity::getTourPlaceId).collect(Collectors.toSet());
        List<Long> mustIds = must.stream().map(TourPlaceEntity::getTourPlaceId).toList();
        Map<Long, TourPlaceCategory> categories = new HashMap<>();
        daytime.forEach(place -> categories.put(place.getTourPlaceId(), place.getCategory()));
        int maxMeals = values(dto, PlannerBlockType.TRAVEL_TYPE).contains("미식") ? 3 : 2;
        List<List<Long>> days = capMeals(toSlots(answer, dates, perDay, daytimeIds, mustIds),
                maxMeals, daytime, Set.copyOf(mustIds)).stream()
                .map(day -> spreadMeals(day, categories))
                .map(ArrayList::new)
                .collect(Collectors.toCollection(ArrayList::new));
        prioritizeFirstDayDeparture(days, daytime, departure);
        Long lodgingId = pickLodging(answer, lodgings.stream()
                .map(TourPlaceEntity::getTourPlaceId).collect(Collectors.toSet()));
        return new GeneratedSchedule(city, dates, days, lodgingId, departure);
    }

    /** 날짜별 일정 카드 */
    @Transactional(readOnly = true)
    public PlannerScheduleResponseDto getSchedule(Long plannerId, String userId) {
        TravelGroupEntity group = travelGroupRepository.findById(plannerId)
                .orElseThrow(() -> new NotFoundException("플래너가 존재하지 않습니다."));
        if (!groupMemberRepository.existsByGroupIdAndUserId(plannerId, userId)) {
            throw new ForbiddenException("해당 플래너의 멤버가 아닙니다.");
        }
        GroupTravelPlanEntity plan = latestPlan(plannerId);

        List<PlannerScheduleSlotEntity> slots =
                slotRepository.findByPlanIdOrderByVisitDateAscSortOrderAsc(plan.getPlanId());
        Map<Long, TourPlaceEntity> places = tourPlaceRepository
                .findAllById(slots.stream()
                        .map(PlannerScheduleSlotEntity::getTourPlaceId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.toMap(TourPlaceEntity::getTourPlaceId, Function.identity()));
        Map<LocalDate, List<PlannerScheduleSlotEntity>> byDate = slots.stream()
                .collect(Collectors.groupingBy(PlannerScheduleSlotEntity::getVisitDate));

        boolean withRoute = group.getStatus() != GroupStatus.PLANNING;
        // 카드가 없는 날도 빠지지 않게 여행 기간으로 돈다
        List<PlannerScheduleResponseDto.Day> days = datesOf(plan.getStartDate(), plan.getEndDate())
                .stream()
                .map(date -> new PlannerScheduleResponseDto.Day(date,
                        byDate.getOrDefault(date, List.of()).stream()
                                .map(slot -> new PlannerScheduleResponseDto.Slot(
                                        slot.getSlotId(), slot.getSortOrder(),
                                        toPlace(places.get(slot.getTourPlaceId())),
                                        withRoute ? slot.getRouteStatus() : null,
                                        withRoute ? toRoute(slot.getRouteData()) : null))
                                .toList()))
                .toList();

        PlannerScheduleResponseDto.Departure departure = plan.getDeparturePlaceName() == null
                ? null
                : new PlannerScheduleResponseDto.Departure(plan.getDeparturePlaceName(),
                        plan.getDepartureLatitude(), plan.getDepartureLongitude());
        return new PlannerScheduleResponseDto(
                plannerId, group.getGroupName(), plan.getCityName(),
                plan.getStartDate(), plan.getEndDate(), days, departure);
    }

    /** 저장된 설문 응답. 멤버만 조회할 수 있다. */
    @Transactional(readOnly = true)
    public PlannerSurveyResponseDto getSurvey(Long plannerId, String userId) {
        if (!groupMemberRepository.existsByGroupIdAndUserId(plannerId, userId)) {
            throw new ForbiddenException("해당 플래너의 멤버가 아닙니다.");
        }
        GroupTravelPlanEntity plan = latestPlan(plannerId);
        return new PlannerSurveyResponseDto(plannerId, deserializeSurvey(plan.getSurveyResponses()));
    }

    /** 설문 응답을 이용해 일정을 다시 생성한다. 직접 수정한 일정은 덮어쓰지 않는다. */
    public PlannerScheduleResponseDto regenerate(Long plannerId, String userId) {
        TravelGroupEntity group = travelGroupRepository.findById(plannerId)
                .orElseThrow(() -> new NotFoundException("플래너가 존재하지 않습니다."));
        requireOwner(group, userId);
        requirePlanning(group);
        GroupTravelPlanEntity plan = latestPlan(plannerId);
        if (Boolean.TRUE.equals(plan.getScheduleEdited())) {
            throw new IllegalArgumentException("직접 수정한 일정은 다시 생성할 수 없습니다.");
        }
        List<PlannerSurveyResponseDto.Block> savedBlocks = deserializeSurvey(plan.getSurveyResponses());
        if (savedBlocks.isEmpty()) {
            throw new IllegalArgumentException("저장된 설문 응답이 없어 일정을 다시 만들 수 없습니다.");
        }
        reserveRegeneration(plannerId, userId, plan.getPlanId());

        GeneratePlannerRequestDto request = requestFromSavedSurvey(group, plan, savedBlocks);
        List<String> memberIds = groupMemberRepository.findByGroupIdOrderByJoinedAtAsc(plannerId)
                .stream().map(member -> member.getUserId()).distinct().toList();
        TravelPreferenceResponseDto preference = TravelPreferenceResponseDto.aggregate(
                travelPreferenceService.getPreferences(memberIds).values());
        GeneratedSchedule generated = buildSchedule(request, preference);

        transactionTemplate.executeWithoutResult(status -> {
            TravelGroupEntity locked = travelGroupRepository.findByIdForUpdate(plannerId)
                    .orElseThrow(() -> new NotFoundException("플래너가 존재하지 않습니다."));
            requireOwner(locked, userId);
            requirePlanning(locked);
            GroupTravelPlanEntity current = latestPlan(plannerId);
            if (!Objects.equals(current.getPlanId(), plan.getPlanId())) {
                throw new IllegalArgumentException("여행 계획이 변경되었습니다. 다시 조회해주세요.");
            }
            if (Boolean.TRUE.equals(current.getScheduleEdited())) {
                throw new IllegalArgumentException("직접 수정한 일정은 다시 생성할 수 없습니다.");
            }
            slotRepository.deleteByPlanId(current.getPlanId());
            slotRepository.saveAll(toEntities(current.getPlanId(), generated.dates(),
                    generated.days(), generated.lodgingId()));
        });
        return getSchedule(plannerId, userId);
    }

    /** 같은 플래너에서 중복 요청으로 AI 비용이 연속 발생하지 않도록 잠금 안에서 예약한다. */
    private void reserveRegeneration(Long plannerId, String userId, Long expectedPlanId) {
        transactionTemplate.executeWithoutResult(status -> {
            TravelGroupEntity locked = travelGroupRepository.findByIdForUpdate(plannerId)
                    .orElseThrow(() -> new NotFoundException("플래너가 존재하지 않습니다."));
            requireOwner(locked, userId);
            requirePlanning(locked);
            GroupTravelPlanEntity current = latestPlan(plannerId);
            if (!Objects.equals(current.getPlanId(), expectedPlanId)) {
                throw new IllegalArgumentException("여행 계획이 변경되었습니다. 다시 조회해주세요.");
            }
            if (Boolean.TRUE.equals(current.getScheduleEdited())) {
                throw new IllegalArgumentException("직접 수정한 일정은 다시 생성할 수 없습니다.");
            }
            LocalDateTime now = LocalDateTime.now();
            if (current.getRegenerationStartedAt() != null
                    && current.getRegenerationStartedAt().isAfter(
                            now.minusSeconds(REGENERATION_COOLDOWN_SECONDS))) {
                throw new IllegalArgumentException("일정 재생성은 30초에 한 번만 요청할 수 있습니다.");
            }
            current.setRegenerationStartedAt(now);
            groupTravelPlanRepository.save(current);
        });
    }

    private GeneratePlannerRequestDto requestFromSavedSurvey(
            TravelGroupEntity group,
            GroupTravelPlanEntity plan,
            List<PlannerSurveyResponseDto.Block> savedBlocks) {
        GeneratePlannerRequestDto request = new GeneratePlannerRequestDto();
        request.setTitle(group.getGroupName());
        request.setMemberCount(group.getHeadcount());
        request.setIsSolo(group.getHeadcount() != null && group.getHeadcount() == 1);
        request.setRegionCode(plan.getRegionCode());
        request.setCityName(plan.getCityName());
        request.setStartDate(plan.getStartDate());
        request.setEndDate(plan.getEndDate());
        request.setBlocks(savedBlocks.stream().map(PlannerSurveyResponseDto.Block::toRequest).toList());
        if (plan.getDeparturePlaceName() != null) {
            GeneratePlannerRequestDto.DeparturePoint departure =
                    new GeneratePlannerRequestDto.DeparturePoint();
            departure.setPlaceName(plan.getDeparturePlaceName());
            departure.setLatitude(plan.getDepartureLatitude());
            departure.setLongitude(plan.getDepartureLongitude());
            request.setDeparturePoint(departure);
        }
        return request;
    }

    private void requireOwner(TravelGroupEntity group, String userId) {
        if (!Objects.equals(group.getOwnerUserId(), userId)) {
            throw new ForbiddenException("플래너 소유자만 일정을 다시 생성할 수 있습니다.");
        }
    }

    private void requirePlanning(TravelGroupEntity group) {
        if (group.getStatus() != GroupStatus.PLANNING) {
            throw new IllegalArgumentException("확정 전 일정만 다시 생성할 수 있습니다.");
        }
    }

    private String serializeSurvey(List<GeneratePlannerRequestDto.Block> blocks) {
        try {
            return objectMapper.writeValueAsString(blocks.stream()
                    .map(PlannerSurveyResponseDto.Block::fromRequest).toList());
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("설문 응답을 저장할 수 없습니다.", exception);
        }
    }

    private List<PlannerSurveyResponseDto.Block> deserializeSurvey(String json) {
        if (json == null || json.isBlank()) return List.of();
        try {
            return objectMapper.readValue(json,
                    new TypeReference<List<PlannerSurveyResponseDto.Block>>() {});
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("저장된 설문 응답을 읽을 수 없습니다.", exception);
        }
    }

    private PlannerScheduleResponseDto.RouteLeg toRoute(String json) {
        if (json == null || json.isBlank()) return null;
        try {
            return objectMapper.readValue(json, PlannerScheduleResponseDto.RouteLeg.class);
        } catch (JsonProcessingException exception) {
            return null;
        }
    }

    private DepartureDetails departureDetails(
            GeneratePlannerRequestDto dto, TravelPreferenceResponseDto preference) {
        GeneratePlannerRequestDto.Block block = dto.getBlocks().stream()
                .filter(item -> item.getType() == PlannerBlockType.DEPARTURE_PLACE)
                .findFirst().orElse(null);
        GeneratePlannerRequestDto.DeparturePoint point = dto.getDeparturePoint();
        if (point == null && block != null
                && block.getLatitude() != null && block.getLongitude() != null) {
            point = new GeneratePlannerRequestDto.DeparturePoint();
            point.setPlaceName(block.getPlaceName() == null || block.getPlaceName().isBlank()
                    ? block.getValue() : block.getPlaceName());
            point.setLatitude(block.getLatitude());
            point.setLongitude(block.getLongitude());
        }
        if (point == null && usesHome(block == null ? null : block.getValue())
                && preference.getHome() != null) {
            TravelPreferenceResponseDto.Home home = preference.getHome();
            point = new GeneratePlannerRequestDto.DeparturePoint();
            point.setPlaceName(home.name());
            point.setLatitude(home.latitude());
            point.setLongitude(home.longitude());
        }
        if (point != null) {
            return new DepartureDetails(point.getPlaceName().strip(),
                    point.getLatitude(), point.getLongitude());
        }
        if (block != null) {
            String name = block.getPlaceName() == null || block.getPlaceName().isBlank()
                    ? block.getValue().strip() : block.getPlaceName().strip();
            return new DepartureDetails(name, block.getLatitude(), block.getLongitude());
        }
        return null;
    }

    static Integer walkingLimit(
            GeneratePlannerRequestDto dto, TravelPreferenceResponseDto preference) {
        Integer configured = TravelPreferenceResponseDto.walkingLimit(preference);
        Integer survey = dto.getBlocks().stream()
                .filter(block -> block.getType() == PlannerBlockType.WALKING)
                .map(block -> walkingMinutes(block.getValue()))
                .filter(Objects::nonNull)
                .min(Integer::compareTo).orElse(null);
        if (configured == null) return survey;
        return survey == null ? configured : Math.min(configured, survey);
    }

    private static Integer walkingMinutes(String value) {
        if (value == null) return null;
        return switch (value.strip()) {
            case "10분 이내", "걷기 최소화" -> 10;
            case "30분 이내", "조금 걷기" -> 30;
            case "1시간 이상", "충분히 걷기" -> 60;
            default -> null;
        };
    }

    static void prioritizeFirstDayDeparture(
            List<List<Long>> days, List<TourPlaceEntity> places, DepartureDetails departure) {
        if (days.isEmpty() || departure == null || !departure.hasCoordinates()) return;
        Map<Long, TourPlaceEntity> byId = places.stream().collect(Collectors.toMap(
                TourPlaceEntity::getTourPlaceId, Function.identity(), (first, ignored) -> first));
        List<Long> firstDay = days.get(0);
        int nearestIndex = -1;
        double nearestDistance = Double.MAX_VALUE;
        for (int i = 0; i < firstDay.size(); i++) {
            TourPlaceEntity place = byId.get(firstDay.get(i));
            if (place == null || place.getLatitude() == null || place.getLongitude() == null) continue;
            double distance = OdsayTransitRouteClient.distanceMeters(
                    departure.latitude(), departure.longitude(),
                    place.getLatitude(), place.getLongitude());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestIndex = i;
            }
        }
        if (nearestIndex > 0) Collections.swap(firstDay, 0, nearestIndex);
    }

    static List<List<Long>> capMeals(List<List<Long>> days, int maxMeals,
            List<TourPlaceEntity> candidates, Set<Long> mustInclude) {
        Map<Long, TourPlaceEntity> byId = new HashMap<>();
        candidates.forEach(place -> byId.put(place.getTourPlaceId(), place));
        Set<Long> used = new HashSet<>();
        days.forEach(day -> day.stream().filter(Objects::nonNull).forEach(used::add));

        List<List<Long>> result = new ArrayList<>();
        for (List<Long> day : days) {
            List<Long> slots = new ArrayList<>(day);
            int meals = 0;
            for (int i = 0; i < slots.size(); i++) {
                TourPlaceEntity place = byId.get(slots.get(i));
                if (place == null || place.getCategory() != TourPlaceCategory.RESTAURANT) continue;
                if (++meals <= maxMeals || mustInclude.contains(place.getTourPlaceId())) continue;
                TourPlaceEntity previous = i > 0 ? byId.get(slots.get(i - 1)) : null;
                TourPlaceEntity replacement = nearestUnused(previous == null ? place : previous,
                        candidates, used);
                slots.set(i, replacement == null ? null : replacement.getTourPlaceId());
                if (replacement != null) used.add(replacement.getTourPlaceId());
            }
            result.add(slots);
        }
        return result;
    }

    private static TourPlaceEntity nearestUnused(TourPlaceEntity from, List<TourPlaceEntity> candidates,
            Set<Long> used) {
        TourPlaceEntity best = null;
        double bestMeters = Double.MAX_VALUE;
        for (TourPlaceEntity place : candidates) {
            if (used.contains(place.getTourPlaceId())
                    || place.getCategory() == TourPlaceCategory.RESTAURANT
                    || place.getCategory() == TourPlaceCategory.ACCOMMODATION) continue;
            double meters = from.getLatitude() == null || from.getLongitude() == null
                    || place.getLatitude() == null || place.getLongitude() == null
                    ? Double.MAX_VALUE - 1
                    : OdsayTransitRouteClient.distanceMeters(from.getLatitude(), from.getLongitude(),
                            place.getLatitude(), place.getLongitude());
            if (meters < bestMeters) {
                best = place;
                bestMeters = meters;
            }
        }
        return best;
    }

    /**
     * 같은 날 맛집끼리 · 카페끼리 붙어 있으면 뒤쪽 장소를 그 뒤의 다른 분류 장소와 바꾼다.
     * 뒤에 바꿀 장소가 없으면 앞쪽에서 같은 분류와 닿지 않는 자리로 옮긴다.
     * 프롬프트 규칙만으로는 AI 가 자주 어긴다. 둘 다 안 되면 그대로 둔다
     */
    static List<Long> spreadMeals(List<Long> day, Map<Long, TourPlaceCategory> categories) {
        List<Long> result = new ArrayList<>(day);
        for (int i = 1; i < result.size(); i++) {
            TourPlaceCategory current = categories.get(result.get(i));
            boolean meal = current == TourPlaceCategory.RESTAURANT || current == TourPlaceCategory.CAFE;
            if (!meal || current != categories.get(result.get(i - 1))) continue;
            boolean moved = false;
            for (int j = i + 1; j < result.size() && !moved; j++) {
                if (categories.get(result.get(j)) != current) {
                    Collections.swap(result, i, j);
                    moved = true;
                }
            }
            // 하루 끝에 몰려 뒤에 바꿀 곳이 없으면, 앞쪽 빈틈 중 원래 자리에서 가까운 곳으로 옮긴다
            for (int k = i - 2; k >= 0 && !moved; k--) {
                boolean clearBefore = k == 0 || categories.get(result.get(k - 1)) != current;
                if (clearBefore && categories.get(result.get(k)) != current) {
                    result.add(k, result.remove(i));
                    moved = true;
                }
            }
        }
        return result;
    }

    static List<List<Long>> toSlots(
            JsonNode answer,
            List<LocalDate> dates,
            int perDay,
            Set<Long> candidateIds,
            List<Long> mustInclude
    ) {
        List<JsonNode> nodes = new ArrayList<>();
        answer.path("days").forEach(nodes::add);
        JsonNode[] assigned = new JsonNode[dates.size()];
        Set<JsonNode> taken = Collections.newSetFromMap(new IdentityHashMap<>());
        for (int i = 0; i < dates.size(); i++) {
            String date = dates.get(i).toString();
            for (JsonNode node : nodes) {
                if (!taken.contains(node) && date.equals(node.path("date").asText())) {
                    assigned[i] = node;
                    taken.add(node);
                    break;
                }
            }
        }
        Iterator<JsonNode> rest = nodes.stream().filter(node -> !taken.contains(node)).iterator();
        for (int i = 0; i < dates.size(); i++) {
            if (assigned[i] == null && rest.hasNext()) {
                assigned[i] = rest.next();
            }
        }

        Set<Long> used = new HashSet<>();
        List<List<Long>> result = new ArrayList<>();
        for (JsonNode day : assigned) {
            List<Long> slots = new ArrayList<>();
            if (day != null) {
                for (JsonNode id : day.path("placeIds")) {
                    if (slots.size() == perDay) {
                        break;
                    }
                    if (id.canConvertToLong() && candidateIds.contains(id.asLong()) && used.add(id.asLong())) {
                        slots.add(id.asLong());
                    }
                }
            }
            while (slots.size() < perDay) {
                slots.add(null);
            }
            result.add(slots);
        }

        Set<Long> must = new HashSet<>(mustInclude);
        for (Long id : mustInclude) {
            if (!used.add(id)) {
                continue;
            }
            if (!place(result, null, id)) {
                // 빈 칸이 없다. 필수가 아닌 칸을 뒤에서부터 찾아 바꾼다
                replaceLastNonMust(result, must, id);
            }
        }
        return result;
    }

    private static boolean place(List<List<Long>> days, Long target, Long id) {
        for (List<Long> day : days) {
            int at = day.indexOf(target);
            if (at >= 0) {
                day.set(at, id);
                return true;
            }
        }
        return false;
    }

    private static void replaceLastNonMust(List<List<Long>> days, Set<Long> must, Long id) {
        for (int d = days.size() - 1; d >= 0; d--) {
            List<Long> day = days.get(d);
            for (int i = day.size() - 1; i >= 0; i--) {
                if (!must.contains(day.get(i))) {
                    day.set(i, id);
                    return;
                }
            }
        }
    }

    static Long pickLodging(JsonNode answer, Set<Long> lodgingIds) {
        JsonNode id = answer.path("lodgingId");
        return id.canConvertToLong() && lodgingIds.contains(id.asLong()) ? id.asLong() : null;
    }

    /** "하루 1~2곳" → 2, "3~4곳" → 4, "5" → 5. 숫자가 없으면 여행 편의 설정 값 */
    static int slotsPerDay(String density, Integer fallback) {
        int count = fallback == null ? TravelPreferenceService.DEFAULT_DAILY_SCHEDULE_COUNT : fallback;
        if (density != null) {
            Matcher m = NUMBER.matcher(density);
            while (m.find()) {
                count = Integer.parseInt(m.group());
            }
        }
        return Math.max(1, Math.min(MAX_PER_DAY, count));
    }

    /** 이름이 서로를 포함하면 같은 곳으로 본다. 공백은 무시한다 */
    static boolean matchesAny(String placeName, List<String> names) {
        if (placeName == null) {
            return false;
        }
        String place = placeName.replace(" ", "");
        return names.stream()
                .map(name -> name.replace(" ", ""))
                .filter(name -> name.length() >= 2)
                .anyMatch(name -> place.contains(name) || name.contains(place));
    }

    // ── 프롬프트 ────────────────────────────────────────────

    static final String SYSTEM_PROMPT = """
            너는 60대 이상 시니어를 위한 국내 여행 일정을 짜는 도우미다.
            반드시 주어진 후보 장소 안에서만 고르고, 후보에 없는 장소를 만들지 않는다.
            JSON 으로만 답한다. 형식:
            {"days":[{"date":"YYYY-MM-DD","placeIds":[후보 id, ...]}],"lodgingId":숙소 후보 id 또는 null}
            규칙:
            - 날마다 placeIds 는 "하루 장소 수" 이하로, 방문 순서대로 적는다. 가까운 곳끼리 잇는다
            - 같은 장소를 두 번 넣지 않는다
            - 날마다 맛집을 최소 한 곳 넣는다. 점심 맛집은 2번째나 3번째 자리에 둔다
            - 하루 장소 수가 4 이상이면 저녁 맛집을 마지막 자리에 하나 더 둔다
            - 맛집끼리, 카페끼리 바로 이어 붙이지 않는다. 사이에 둘러볼 곳을 둔다
            - 숙소는 placeIds 에 넣지 않는다. 숙소 후보가 있으면 lodgingId 로 한 곳만 고른다
            - 맛집 규칙 말고는, 지침을 지킬 장소가 없으면 억지로 채우지 말고 개수를 줄인다
            - 반드시 포함할 곳이 후보에 있으면 꼭 넣는다
            """;

    private String userPrompt(
            GeneratePlannerRequestDto dto,
            String city,
            List<LocalDate> dates,
            int perDay,
            TravelPreferenceResponseDto preference,
            List<TourPlaceEntity> daytime,
            List<TourPlaceEntity> lodgings,
            Set<Long> barrierFree,
            DepartureDetails departure,
            Integer walkingLimit
    ) {
        StringBuilder b = new StringBuilder();
        int people = Boolean.TRUE.equals(dto.getIsSolo()) ? 1 : dto.getMemberCount();
        b.append("여행지: ").append(city).append(" · ").append(people).append("명\n");
        b.append("날짜: ").append(dates.stream().map(LocalDate::toString)
                .collect(Collectors.joining(", "))).append('\n');
        b.append("하루 장소 수: ").append(perDay).append("\n\n");
        if (departure != null) {
            b.append("출발지: ").append(departure.name());
            if (departure.hasCoordinates()) {
                b.append(" (위도 ").append(departure.latitude())
                        .append(", 경도 ").append(departure.longitude()).append(")");
                b.append("\n첫날의 첫 방문지는 이 출발지에서 좌표상 가장 가까운 후보 장소로 정한다.\n");
            } else {
                b.append('\n');
            }
            b.append('\n');
        }

        b.append("지침:\n");
        Map<PlannerBlockType, List<String>> blocks = new LinkedHashMap<>();
        dto.getBlocks().forEach(block ->
                blocks.computeIfAbsent(block.getType(), k -> new ArrayList<>()).add(block.getValue().trim()));
        if (blocks.isEmpty()) {
            b.append("- 없음\n");
        }
        blocks.forEach((type, values) ->
                b.append("- ").append(type.getLabel()).append(": ")
                        .append(String.join(", ", values)).append('\n'));

        // 여행 편의 설정(#145). 같은 내용의 블록이 있으면 블록이 이긴다
        b.append("\n기본 설정 (같은 내용의 지침이 있으면 지침을 따른다):\n");
        b.append("- 선호 이동수단: ").append(preference.getPreferredTransport()).append('\n');
        b.append("- 계단 이용: ").append(Boolean.FALSE.equals(preference.getCanUseStairs())
                ? "어려움" : "가능").append('\n');
        List<AvoidFood> avoidFoods = preference.getAvoidFoods();
        if (avoidFoods != null && !avoidFoods.isEmpty()) {
            b.append("- 피하는 음식: ").append(avoidFoods.stream().map(AvoidFood::getLabel)
                    .collect(Collectors.joining(", ")))
                    .append(" (이름으로 보아 이런 음식이 주메뉴인 맛집은 고르지 않는다)\n");
        }
        if (Boolean.TRUE.equals(preference.getUsesWheelchair())) {
            b.append("- 휠체어 이용: 예 (계단 · 비탈 · 흙길이 많은 곳은 고르지 않는다. 무장애 표시가 있는 곳을 우선한다)\n");
        }
        if (Boolean.TRUE.equals(preference.getLimitedWalking())) {
            b.append("- 오래 걷기: 어려움 (넓은 공원 · 산 · 긴 산책로처럼 많이 걸어야 하는 곳은 피하고, 가까운 곳끼리 잇는다)\n");
        }
        if (walkingLimit != null) {
            b.append("- 한 번에 걸을 수 있는 시간: ")
                    .append(walkingLimit >= 60 ? "1시간 이상" : walkingLimit + "분 이내")
                    .append(". 이 기준을 넘게 걷는 구간은 피하고 장소를 가까운 순서로 배치한다.\n");
        }
        b.append('\n');

        b.append("후보 장소 (id|이름|분류|위도,경도|평점|무장애|리뷰 수):\n");
        daytime.forEach(place -> b.append(line(place, barrierFree)));
        b.append("\n숙소 후보:\n");
        if (lodgings.isEmpty()) {
            b.append("- 없음 (lodgingId 는 null)\n");
        }
        lodgings.forEach(place -> b.append(line(place, barrierFree)));
        return b.toString();
    }

    private static String line(TourPlaceEntity place, Set<Long> barrierFree) {
        return place.getTourPlaceId() + "|" + place.getPlaceName() + "|"
                + (place.getCategory() == null ? "" : place.getCategory().getLabel()) + "|"
                + place.getLatitude() + "," + place.getLongitude() + "|"
                + (place.getRating() == null ? "" : place.getRating()) + "|"
                + (barrierFree.contains(place.getTourPlaceId()) ? "무장애" : "") + "|"
                + (place.getUserRatingCount() == null ? 0 : place.getUserRatingCount()) + "\n";
    }

    private Set<Long> barrierFreeIds(
            TravelPreferenceResponseDto preference, List<TourPlaceEntity> places,
            Integer walkingLimit) {
        if (!Boolean.TRUE.equals(preference.getUsesWheelchair())
                && (walkingLimit == null || walkingLimit >= 60)) return Set.of();
        return accessibilityRepository.findAllById(places.stream()
                        .map(TourPlaceEntity::getTourPlaceId).toList())
                .stream()
                .filter(row -> row.getContentId() != null && !"[]".equals(row.getItemsJson()))
                .map(TourPlaceAccessibilityEntity::getTourPlaceId)
                .collect(Collectors.toSet());
    }

    // ── 나머지 ──────────────────────────────────────────────

    private JsonNode parse(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new AiUnavailableException("AI가 일정을 만들지 못했어요. 잠시 후 다시 시도해주세요.");
        }
    }

    private static List<LocalDate> datesOf(LocalDate start, LocalDate end) {
        long days = ChronoUnit.DAYS.between(start, end) + 1;
        if (days < 1) {
            throw new IllegalArgumentException("여행 종료일은 시작일보다 빠를 수 없습니다.");
        }
        if (days > MAX_DAYS) {
            throw new IllegalArgumentException("여행 기간은 " + MAX_DAYS + "일까지 만들 수 있어요.");
        }
        return start.datesUntil(end.plusDays(1)).toList();
    }

    private static List<String> values(GeneratePlannerRequestDto dto, PlannerBlockType type) {
        return dto.getBlocks().stream()
                .filter(block -> block.getType() == type)
                .map(block -> block.getValue().trim())
                .toList();
    }

    private static String last(List<String> values) {
        return values.isEmpty() ? null : values.get(values.size() - 1);
    }

    private GroupTravelPlanEntity latestPlan(Long plannerId) {
        return groupTravelPlanRepository.findFirstByGroupIdOrderByCreatedAtDesc(plannerId)
                .orElseThrow(() -> new NotFoundException("플래너의 여행 계획이 없습니다."));
    }

    private static CreatePlannerRequestDto toCreateRequest(GeneratePlannerRequestDto dto, String city) {
        CreatePlannerRequestDto create = new CreatePlannerRequestDto();
        create.setTitle(dto.getTitle().trim());
        create.setMemberCount(dto.getMemberCount());
        create.setIsSolo(dto.getIsSolo());
        create.setRegionCode(dto.getRegionCode());
        create.setCityName(city);
        create.setStartDate(dto.getStartDate());
        create.setEndDate(dto.getEndDate());
        return create;
    }

    /** 출발 장소 블록을 안 골랐거나 "집 근처" 를 골랐을 때만 집을 출발지로 쓴다 */
    static boolean usesHome(String departureBlockValue) {
        return departureBlockValue == null || departureBlockValue.isBlank()
                || departureBlockValue.strip().equals("집 근처");
    }

    /** 숙소는 마지막 날을 빼고 날마다 맨 끝 칸에 둔다. 그날 밤 자는 곳이다 */
    static List<PlannerScheduleSlotEntity> toEntities(
            Long planId, List<LocalDate> dates, List<List<Long>> days, Long lodgingId) {
        List<PlannerScheduleSlotEntity> rows = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            List<Long> day = days.get(i);
            for (int j = 0; j < day.size(); j++) {
                rows.add(new PlannerScheduleSlotEntity(planId, dates.get(i), j + 1, day.get(j)));
            }
            if (lodgingId != null && i < dates.size() - 1) {
                rows.add(new PlannerScheduleSlotEntity(planId, dates.get(i), day.size() + 1, lodgingId));
            }
        }
        return rows;
    }

    static PlannerScheduleResponseDto.Place toPlace(TourPlaceEntity place) {
        if (place == null) {
            return null;
        }
        TourPlaceCategory category = place.getCategory();
        return new PlannerScheduleResponseDto.Place(
                place.getTourPlaceId(), place.getPlaceName(),
                category == null ? null : category.name(),
                category == null ? null : category.getLabel(),
                place.getImageUrl(), place.getAddress(), place.getRating(),
                place.getLatitude(), place.getLongitude(),
                place.getDescription(), place.getPrimaryTypeLabel());
    }
}
