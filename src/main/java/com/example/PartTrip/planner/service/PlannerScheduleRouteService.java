package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.planner.dto.response.PlannerScheduleResponseDto;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.PlannerScheduleSlotEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.PlannerScheduleSlotRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.profile.dto.TravelPreferenceResponseDto;
import com.example.PartTrip.profile.enums.PreferredTransport;
import com.example.PartTrip.profile.service.TravelPreferenceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/** 일정이 변경될 때만 경로를 계산하고 결과를 슬롯에 캐시한다. */
@Service
@RequiredArgsConstructor
public class PlannerScheduleRouteService {

    private static final Logger log = LoggerFactory.getLogger(PlannerScheduleRouteService.class);
    private static final String CALCULATING = "CALCULATING";

    private final TravelGroupRepository groupRepository;
    private final GroupTravelPlanRepository planRepository;
    private final PlannerScheduleSlotRepository slotRepository;
    private final TourPlaceRepository placeRepository;
    private final TravelPreferenceService travelPreferenceService;
    private final OdsayTransitRouteClient odsayClient;
    private final OdsayDailyCallBudget odsayDailyCallBudget;
    private final GoogleDrivingRouteClient googleRouteClient;
    private final ObjectMapper objectMapper;
    private final TransactionTemplate transactionTemplate;
    private final PlannerRouteTaskRunner taskRunner;

    /** 경로 상태를 즉시 갱신하고, 외부 API 계산을 백그라운드에 예약한다. */
    public void recalculate(Long plannerId) {
        RouteSnapshot snapshot = transactionTemplate.execute(status -> prepare(plannerId));
        if (snapshot == null || snapshot.pending().isEmpty()) return;
        try {
            taskRunner.submit(() -> calculateAndPersist(snapshot));
        } catch (TaskRejectedException exception) {
            log.warn("일정 경로 작업 대기열이 가득 차 경로 계산을 예약하지 못했습니다. plannerId={}", plannerId);
            persistFailure(snapshot);
        }
    }

    /** 잠금 트랜잭션에서 입력 스냅샷을 만들고, 계산 대기 상태를 기록한다. */
    private RouteSnapshot prepare(Long plannerId) {
        TravelGroupEntity group = groupRepository.findByIdForUpdate(plannerId).orElse(null);
        if (group == null) return null;
        GroupTravelPlanEntity plan = planRepository
                .findFirstByGroupIdOrderByCreatedAtDesc(plannerId).orElse(null);
        if (plan == null) return null;

        List<PlannerScheduleSlotEntity> slots = slotRepository
                .findByPlanIdOrderByVisitDateAscSortOrderAsc(plan.getPlanId());
        if (slots.isEmpty()) return null;
        Map<Long, TourPlaceEntity> places = loadPlaces(slots);
        PreferredTransport mode = preferredTransport(group.getOwnerUserId());
        List<RouteInput> inputs = buildInputs(plan, mode, slots, places);
        Map<Long, RouteInput> inputsBySlot = inputs.stream()
                .collect(Collectors.toMap(RouteInput::slotId, Function.identity()));
        Map<String, RouteCache> reusableRoutes = new HashMap<>();
        List<RouteInput> pending = new ArrayList<>();

        for (PlannerScheduleSlotEntity slot : slots) {
            RouteInput input = inputsBySlot.get(slot.getSlotId());
            if (input == null || !input.hasLeg()) {
                clearRoute(slot);
                continue;
            }
            if (!hasCoordinates(input.originLatitude(), input.originLongitude())
                    || !hasCoordinates(input.destinationLatitude(), input.destinationLongitude())) {
                setRoute(slot, new RouteCache("MISSING_COORDINATES", null, input.signature()));
                reusableRoutes.putIfAbsent(input.signature(),
                        new RouteCache("MISSING_COORDINATES", null, input.signature()));
                continue;
            }
            boolean providerConfigured = isProviderConfigured(mode);
            if (!providerConfigured) {
                RouteCache waiting = new RouteCache("WAITING_FOR_API_KEY", null, input.signature());
                setRoute(slot, waiting);
                reusableRoutes.putIfAbsent(input.signature(), waiting);
                continue;
            }

            RouteCache reusable = reusableRoutes.get(input.signature());
            if (reusable == null
                    && input.signature().equals(slot.getRouteSignature())
                    && isReusableStatus(slot.getRouteStatus())) {
                reusable = new RouteCache(slot.getRouteStatus(), slot.getRouteData(), input.signature());
            }
            if (reusable != null) {
                setRoute(slot, reusable);
                reusableRoutes.putIfAbsent(input.signature(), reusable);
                continue;
            }

            slot.setRouteStatus(CALCULATING);
            slot.setRouteSignature(input.signature());
            slot.setRouteData(null);
            pending.add(input);
        }
        slotRepository.saveAll(slots);
        return new RouteSnapshot(plan.getPlanId(), List.copyOf(inputs), List.copyOf(pending));
    }

    /** 호출한 외부 제공자 결과를 계산하고, 입력이 바뀌지 않은 슬롯에만 저장한다. */
    private void calculateAndPersist(RouteSnapshot snapshot) {
        try {
            Map<Long, RouteCache> routesBySlotId = new HashMap<>();
            Map<String, RouteCache> reusableRoutes = new HashMap<>();
            for (RouteInput input : snapshot.pending()) {
                RouteCache cache = reusableRoutes.get(input.signature());
                if (cache == null) {
                    cache = calculate(input);
                    if (isReusableStatus(cache.status())) {
                        reusableRoutes.put(input.signature(), cache);
                    }
                }
                routesBySlotId.put(input.slotId(), cache);
            }
            persistIfInputsMatch(snapshot, routesBySlotId);
        } catch (RuntimeException exception) {
            log.warn("일정 경로 계산을 완료하지 못했습니다. planId={}", snapshot.planId());
            persistFailure(snapshot);
        }
    }

    /** API 키·한도에 따라 적절한 경로 제공자를 호출한다. */
    private RouteCache calculate(RouteInput input) {
        if (input.mode() == PreferredTransport.PUBLIC_TRANSIT) {
            if (!odsayClient.isConfigured()) {
                return new RouteCache("WAITING_FOR_API_KEY", null, input.signature());
            }
            if (!odsayDailyCallBudget.tryAcquire()) {
                return new RouteCache("DAILY_QUOTA_REACHED", null, input.signature());
            }
            OdsayTransitRouteClient.SearchResult result = odsayClient.search(
                    input.originLongitude(), input.originLatitude(),
                    input.destinationLongitude(), input.destinationLatitude(),
                    input.originName(), input.destinationName());
            return new RouteCache(result.status(), encode(result.route()), input.signature());
        }

        GoogleDrivingRouteClient.SearchResult result = googleRouteClient.search(
                input.mode(), input.originLatitude(), input.originLongitude(),
                input.destinationLatitude(), input.destinationLongitude(),
                input.originName(), input.destinationName());
        return new RouteCache(result.status(), encode(result.route()), input.signature());
    }

    /** 계산 시점의 입력이 현재 슬롯과 같은 경우에만 캐시를 반영한다. */
    private void persistIfInputsMatch(RouteSnapshot snapshot, Map<Long, RouteCache> routesBySlotId) {
        transactionTemplate.executeWithoutResult(status -> {
            GroupTravelPlanEntity snapshotPlan = planRepository.findById(snapshot.planId()).orElse(null);
            if (snapshotPlan == null) return;
            TravelGroupEntity group = groupRepository.findByIdForUpdate(snapshotPlan.getGroupId()).orElse(null);
            if (group == null) return;
            GroupTravelPlanEntity plan = planRepository
                    .findFirstByGroupIdOrderByCreatedAtDesc(group.getGroupId()).orElse(null);
            if (plan == null || !Objects.equals(plan.getPlanId(), snapshot.planId())) return;

            List<PlannerScheduleSlotEntity> currentSlots = slotRepository
                    .findByPlanIdOrderByVisitDateAscSortOrderAsc(plan.getPlanId());
            Map<Long, TourPlaceEntity> places = loadPlaces(currentSlots);
            PreferredTransport mode = preferredTransport(group.getOwnerUserId());
            Map<Long, RouteInput> currentInputs = buildInputs(plan, mode, currentSlots, places).stream()
                    .collect(Collectors.toMap(RouteInput::slotId, Function.identity()));
            Map<Long, RouteInput> expectedInputs = snapshot.inputs().stream()
                    .collect(Collectors.toMap(RouteInput::slotId, Function.identity()));

            boolean changed = false;
            for (PlannerScheduleSlotEntity slot : currentSlots) {
                RouteInput expected = expectedInputs.get(slot.getSlotId());
                RouteInput current = currentInputs.get(slot.getSlotId());
                RouteCache cache = routesBySlotId.get(slot.getSlotId());
                if (expected == null || current == null || !expected.equals(current) || cache == null) {
                    continue;
                }
                setRoute(slot, cache);
                changed = true;
            }
            if (changed) slotRepository.saveAll(currentSlots);
        });
    }

    /** 거부되거나 예기치 않게 실패한 계산을 같은 입력의 API_ERROR로 종료한다. */
    private void persistFailure(RouteSnapshot snapshot) {
        Map<Long, RouteCache> failures = snapshot.pending().stream().collect(Collectors.toMap(
                RouteInput::slotId,
                input -> new RouteCache("API_ERROR", null, input.signature()),
                (first, ignored) -> first));
        persistIfInputsMatch(snapshot, failures);
    }

    /** 계획·교통수단·장소 순서와 출발지로 각 슬롯의 전체 유효 입력을 만든다. */
    private List<RouteInput> buildInputs(GroupTravelPlanEntity plan, PreferredTransport mode,
            List<PlannerScheduleSlotEntity> slots, Map<Long, TourPlaceEntity> places) {
        List<RouteInput> inputs = new ArrayList<>(slots.size());
        Long previousPlaceId = null;
        for (PlannerScheduleSlotEntity slot : slots) {
            TourPlaceEntity destination = places.get(slot.getTourPlaceId());
            TourPlaceEntity origin = places.get(previousPlaceId);
            String originName = null;
            Double originLatitude = null;
            Double originLongitude = null;
            Long originPlaceId = null;
            if (origin != null) {
                originName = origin.getPlaceName();
                originLatitude = origin.getLatitude();
                originLongitude = origin.getLongitude();
                originPlaceId = origin.getTourPlaceId();
            } else if (slot.getVisitDate().equals(plan.getStartDate())) {
                originName = plan.getDeparturePlaceName();
                originLatitude = plan.getDepartureLatitude();
                originLongitude = plan.getDepartureLongitude();
            }
            boolean hasOrigin = origin != null
                    || (slot.getVisitDate().equals(plan.getStartDate())
                    && (originName != null || hasCoordinates(originLatitude, originLongitude)));
            boolean hasLeg = destination != null && hasOrigin;
            String destinationName = destination == null ? null : destination.getPlaceName();
            Double destinationLatitude = destination == null ? null : destination.getLatitude();
            Double destinationLongitude = destination == null ? null : destination.getLongitude();
            Long destinationId = destination == null ? slot.getTourPlaceId() : destination.getTourPlaceId();
            String signature = hasLeg
                    ? routeSignature(mode, originName, originLatitude, originLongitude,
                            destinationName, destinationLatitude, destinationLongitude)
                    : null;
            RouteInput input = new RouteInput(
                    slot.getSlotId(), slot.getVisitDate(), slot.getSortOrder(), slot.getTourPlaceId(),
                    originPlaceId, mode, originName, originLatitude, originLongitude,
                    destinationId, destinationName, destinationLatitude, destinationLongitude,
                    plan.getDeparturePlaceName(), plan.getDepartureLatitude(), plan.getDepartureLongitude(),
                    plan.getStartDate(), plan.getEndDate(), hasLeg, signature);
            inputs.add(input);
            if (destination != null) previousPlaceId = destination.getTourPlaceId();
        }
        return inputs;
    }

    /** 현재 일정에 사용된 장소만 한 번에 조회한다. */
    private Map<Long, TourPlaceEntity> loadPlaces(List<PlannerScheduleSlotEntity> slots) {
        return placeRepository.findAllById(slots.stream()
                        .map(PlannerScheduleSlotEntity::getTourPlaceId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.toMap(TourPlaceEntity::getTourPlaceId, Function.identity()));
    }

    /** 사용자의 저장된 이동 수단을 가져오고 미설정 시 기본 이동 수단을 적용한다. */
    private PreferredTransport preferredTransport(String ownerUserId) {
        TravelPreferenceResponseDto preference = travelPreferenceService.getPreference(ownerUserId);
        return preference.getPreferredTransport() == null
                ? TravelPreferenceService.DEFAULT_TRANSPORT
                : preference.getPreferredTransport();
    }

    /** 이동 수단에 맞는 외부 경로 API 키가 설정되어 있는지 확인한다. */
    private boolean isProviderConfigured(PreferredTransport mode) {
        return mode == PreferredTransport.PUBLIC_TRANSIT
                ? odsayClient.isConfigured()
                : googleRouteClient.isConfigured();
    }

    /** 완료 상태만 캐시로 재사용하고 일시 오류나 대기 상태는 다시 계산한다. */
    private boolean isReusableStatus(String status) {
        return status != null && !CALCULATING.equals(status) && !"API_ERROR".equals(status)
                && !"WAITING_FOR_API_KEY".equals(status) && !"DAILY_QUOTA_REACHED".equals(status);
    }

    /** 경로 상태와 응답 데이터를 슬롯 엔티티에 반영한다. */
    private void setRoute(PlannerScheduleSlotEntity slot, RouteCache cache) {
        slot.setRouteStatus(cache.status());
        slot.setRouteData(cache.routeJson());
        slot.setRouteSignature(cache.signature());
    }

    /** 출발·도착 구간이 없는 슬롯의 이전 경로 데이터를 지운다. */
    private void clearRoute(PlannerScheduleSlotEntity slot) {
        slot.setRouteStatus(null);
        slot.setRouteData(null);
        slot.setRouteSignature(null);
    }

    /** 경로 응답을 슬롯에 저장할 JSON으로 직렬화한다. */
    private String encode(PlannerScheduleResponseDto.RouteLeg route) {
        if (route == null) return null;
        try {
            return objectMapper.writeValueAsString(route);
        } catch (JsonProcessingException exception) {
            log.warn("일정 경로 캐시를 직렬화하지 못했습니다.");
            return null;
        }
    }

    /** 위도와 경도가 모두 존재하는지 검사한다. */
    private boolean hasCoordinates(Double latitude, Double longitude) {
        return latitude != null && longitude != null;
    }

    /** 경로 캐시를 재사용할 수 있도록 제공자·출발지·도착지를 안정된 서명으로 만든다. */
    private String routeSignature(PreferredTransport mode, String fromName, Double fromLatitude,
            Double fromLongitude, String toName, Double toLatitude, Double toLongitude) {
        String value = String.join("|", mode.name(), Objects.toString(fromName, ""),
                Objects.toString(fromLatitude, ""), Objects.toString(fromLongitude, ""),
                Objects.toString(toName, ""), Objects.toString(toLatitude, ""),
                Objects.toString(toLongitude, ""));
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(64);
            for (byte b : digest) hex.append(String.format("%02x", b));
            return hex.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("경로 캐시 서명을 만들 수 없습니다.", exception);
        }
    }

    private record RouteSnapshot(Long planId, List<RouteInput> inputs, List<RouteInput> pending) {}

    /** 동시 변경 검증을 위한 경로 입력 전체와 API 요청에 필요한 값. */
    private record RouteInput(Long slotId, LocalDate visitDate, Integer sortOrder, Long tourPlaceId,
            Long originPlaceId, PreferredTransport mode, String originName,
            Double originLatitude, Double originLongitude, Long destinationPlaceId,
            String destinationName, Double destinationLatitude, Double destinationLongitude,
            String planDepartureName, Double planDepartureLatitude, Double planDepartureLongitude,
            LocalDate planStartDate, LocalDate planEndDate, boolean hasLeg, String signature) {}

    private record RouteCache(String status, String routeJson, String signature) {}
}
