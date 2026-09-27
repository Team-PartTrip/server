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
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/** 일정이 만들어지거나 수정될 때만 경로를 계산해 카드 슬롯에 저장한다. */
@Service
@RequiredArgsConstructor
public class PlannerScheduleRouteService {

    private static final Logger log = LoggerFactory.getLogger(PlannerScheduleRouteService.class);

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

    public void recalculate(Long plannerId) {
        TravelGroupEntity group = groupRepository.findById(plannerId).orElse(null);
        if (group == null) return;
        GroupTravelPlanEntity plan = planRepository
                .findFirstByGroupIdOrderByCreatedAtDesc(plannerId).orElse(null);
        if (plan == null) return;

        List<PlannerScheduleSlotEntity> slots = slotRepository
                .findByPlanIdOrderByVisitDateAscSortOrderAsc(plan.getPlanId());
        if (slots.isEmpty()) return;

        Map<Long, TourPlaceEntity> places = placeRepository.findAllById(slots.stream()
                        .map(PlannerScheduleSlotEntity::getTourPlaceId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.toMap(TourPlaceEntity::getTourPlaceId, Function.identity()));
        TravelPreferenceResponseDto preference = travelPreferenceService
                .getPreference(group.getOwnerUserId());
        PreferredTransport mode = preference.getPreferredTransport();
        if (mode == null) mode = TravelPreferenceService.DEFAULT_TRANSPORT;

        Map<Long, RouteCache> routesBySlotId = new HashMap<>();
        Map<String, RouteCache> reusableRoutes = new HashMap<>();
        Long previousPlaceId = null;
        for (PlannerScheduleSlotEntity slot : slots) {
            TourPlaceEntity destination = places.get(slot.getTourPlaceId());
            if (destination == null) continue;

            TourPlaceEntity origin = places.get(previousPlaceId);
            String originName;
            Double originLatitude;
            Double originLongitude;
            if (origin != null) {
                originName = origin.getPlaceName();
                originLatitude = origin.getLatitude();
                originLongitude = origin.getLongitude();
            } else if (slot.getVisitDate().equals(plan.getStartDate())) {
                originName = plan.getDeparturePlaceName();
                originLatitude = plan.getDepartureLatitude();
                originLongitude = plan.getDepartureLongitude();
            } else {
                previousPlaceId = destination.getTourPlaceId();
                continue;
            }

            String signature = routeSignature(mode, originName, originLatitude, originLongitude,
                    destination.getPlaceName(), destination.getLatitude(), destination.getLongitude());
            boolean providerConfigured = mode == PreferredTransport.PUBLIC_TRANSIT
                    ? odsayClient.isConfigured()
                    : googleRouteClient.isConfigured();
            RouteCache cached = reusableRoutes.get(signature);
            boolean retryAfterLimit = "DAILY_QUOTA_REACHED".equals(slot.getRouteStatus());
            if (cached == null && signature.equals(slot.getRouteSignature())
                    && slot.getRouteStatus() != null
                    && !("WAITING_FOR_API_KEY".equals(slot.getRouteStatus()) && providerConfigured)
                    && !retryAfterLimit) {
                cached = new RouteCache(slot.getRouteStatus(), slot.getRouteData(), signature);
            }
            if (cached != null) {
                routesBySlotId.put(slot.getSlotId(), cached);
                reusableRoutes.putIfAbsent(signature, cached);
                previousPlaceId = destination.getTourPlaceId();
                continue;
            }

            RouteCache cache;
            if (!hasCoordinates(originLatitude, originLongitude)
                    || !hasCoordinates(destination.getLatitude(), destination.getLongitude())) {
                cache = new RouteCache("MISSING_COORDINATES", null, signature);
            } else if (mode == PreferredTransport.PUBLIC_TRANSIT) {
                if (!odsayClient.isConfigured()) {
                    cache = new RouteCache("WAITING_FOR_API_KEY", null, signature);
                } else if (!odsayDailyCallBudget.tryAcquire()) {
                    cache = new RouteCache("DAILY_QUOTA_REACHED", null, signature);
                } else {
                    OdsayTransitRouteClient.SearchResult result = odsayClient.search(
                            originLongitude, originLatitude,
                            destination.getLongitude(), destination.getLatitude(),
                            originName, destination.getPlaceName());
                    cache = new RouteCache(result.status(), encode(result.route()), signature);
                }
            } else {
                GoogleDrivingRouteClient.SearchResult result = googleRouteClient.search(
                        mode, originLatitude, originLongitude,
                        destination.getLatitude(), destination.getLongitude(),
                        originName, destination.getPlaceName());
                cache = new RouteCache(result.status(), encode(result.route()), signature);
            }
            routesBySlotId.put(slot.getSlotId(), cache);
            if (!"WAITING_FOR_API_KEY".equals(cache.status())
                    && !"DAILY_QUOTA_REACHED".equals(cache.status())) {
                reusableRoutes.put(signature, cache);
            }
            previousPlaceId = destination.getTourPlaceId();
        }

        // API 호출은 트랜잭션 밖에서 끝낸 뒤 캐시만 짧게 저장한다.
        transactionTemplate.executeWithoutResult(status -> {
            List<PlannerScheduleSlotEntity> current = slotRepository
                    .findByPlanIdOrderByVisitDateAscSortOrderAsc(plan.getPlanId());
            for (PlannerScheduleSlotEntity slot : current) {
                RouteCache cache = routesBySlotId.get(slot.getSlotId());
                slot.setRouteStatus(cache == null ? null : cache.status());
                slot.setRouteData(cache == null ? null : cache.routeJson());
                slot.setRouteSignature(cache == null ? null : cache.signature());
            }
            slotRepository.saveAll(current);
        });
    }

    private String encode(PlannerScheduleResponseDto.RouteLeg route) {
        if (route == null) return null;
        try {
            return objectMapper.writeValueAsString(route);
        } catch (JsonProcessingException exception) {
            log.warn("일정 경로 캐시를 직렬화하지 못했습니다.");
            return null;
        }
    }

    private boolean hasCoordinates(Double latitude, Double longitude) {
        return latitude != null && longitude != null;
    }

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

    private record RouteCache(String status, String routeJson, String signature) {}
}
