package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.enums.TourPlaceCategory;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlannerScheduleRouteServiceTest {

    @Mock private TravelGroupRepository groups;
    @Mock private GroupTravelPlanRepository plans;
    @Mock private PlannerScheduleSlotRepository slots;
    @Mock private TourPlaceRepository places;
    @Mock private TravelPreferenceService preferences;
    @Mock private OdsayTransitRouteClient odsay;
    @Mock private OdsayDailyCallBudget odsayBudget;
    @Mock private GoogleDrivingRouteClient google;
    @Mock private TransactionTemplate transactions;
    @Mock private PlannerRouteTaskRunner taskRunner;

    private final Long plannerId = 41L;
    private final Long planId = 72L;
    private final LocalDate startDate = LocalDate.of(2026, 10, 7);
    private TravelGroupEntity group;
    private GroupTravelPlanEntity plan;
    private PlannerScheduleSlotEntity slot;
    private TourPlaceEntity firstPlace;
    private TourPlaceEntity secondPlace;
    private TourPlaceEntity lodging;
    private PlannerScheduleRouteService service;

    @BeforeEach
    void setUp() {
        group = new TravelGroupEntity();
        group.setGroupId(plannerId);
        group.setOwnerUserId("owner");
        plan = new GroupTravelPlanEntity();
        plan.setPlanId(planId);
        plan.setGroupId(plannerId);
        plan.setStartDate(startDate);
        plan.setEndDate(startDate.plusDays(2));
        plan.setDeparturePlaceName("강릉역");
        plan.setDepartureLatitude(37.75);
        plan.setDepartureLongitude(128.90);
        slot = new PlannerScheduleSlotEntity(planId, startDate, 1, 101L);
        slot.setSlotId(501L);
        firstPlace = place(101L, "경포대", 37.80, 128.90);
        secondPlace = place(102L, "오죽헌", 37.78, 128.87);
        lodging = place(103L, "강릉호텔", 37.76, 128.89);
        lodging.setCategory(TourPlaceCategory.ACCOMMODATION);

        doAnswer(invocation -> {
            TransactionCallback<?> callback = invocation.getArgument(0);
            return callback.doInTransaction(mock(TransactionStatus.class));
        }).when(transactions).execute(any(TransactionCallback.class));
        lenient().doAnswer(invocation -> {
            Consumer<TransactionStatus> callback = invocation.getArgument(0);
            callback.accept(mock(TransactionStatus.class));
            return null;
        }).when(transactions).executeWithoutResult(any());

        when(groups.findByIdForUpdate(plannerId)).thenReturn(Optional.of(group));
        when(plans.findFirstByGroupIdOrderByCreatedAtDesc(plannerId)).thenReturn(Optional.of(plan));
        lenient().when(plans.findById(planId)).thenReturn(Optional.of(plan));
        when(slots.findByPlanIdOrderByVisitDateAscSortOrderAsc(planId)).thenAnswer(
                ignored -> List.of(slot));
        when(places.findAllById(anyCollection())).thenAnswer(invocation -> {
            Collection<?> ids = (Collection<?>) invocation.getArgument(0);
            return List.of(firstPlace, secondPlace, lodging).stream()
                    .filter(place -> ids.contains(place.getTourPlaceId())).toList();
        });
        when(preferences.getPreference("owner"))
                .thenReturn(new TravelPreferenceResponseDto(PreferredTransport.PUBLIC_TRANSIT, 4, true));
        when(odsay.isConfigured()).thenReturn(true);
        lenient().when(odsayBudget.tryAcquire()).thenReturn(true);
        lenient().when(odsay.search(any(Double.class), any(Double.class), any(Double.class), any(Double.class),
                any(), any())).thenReturn(new OdsayTransitRouteClient.SearchResult("READY",
                new PlannerScheduleResponseDto.RouteLeg("PUBLIC_TRANSIT", "강릉역", "경포대",
                        25, 5, List.of())));
        service = new PlannerScheduleRouteService(groups, plans, slots, places, preferences,
                odsay, odsayBudget, google, new ObjectMapper(), transactions, taskRunner);
    }

    @Test
    void 첫날_첫_장소는_계획_출발지에서_계산하고_같은_경로는_재사용한다() {
        service.recalculate(plannerId);
        Runnable calculation = captureCalculation();
        calculation.run();

        verify(odsay).search(128.90, 37.75, 128.90, 37.80, "강릉역", "경포대");
        assertThat(slot.getRouteStatus()).isEqualTo("READY");

        clearInvocations(taskRunner, odsay);
        service.recalculate(plannerId);

        verify(taskRunner, never()).submit(any());
        verify(odsay, never()).search(any(Double.class), any(Double.class), any(Double.class),
                any(Double.class), any(), any());
    }

    @Test
    void 둘째날_첫_장소는_전날_묵은_숙소에서_계산한다() {
        PlannerScheduleSlotEntity night = new PlannerScheduleSlotEntity(planId, startDate, 2, 103L);
        night.setSlotId(502L);
        PlannerScheduleSlotEntity nextDay = new PlannerScheduleSlotEntity(planId, startDate.plusDays(1), 1, 102L);
        nextDay.setSlotId(503L);
        when(slots.findByPlanIdOrderByVisitDateAscSortOrderAsc(planId))
                .thenAnswer(ignored -> List.of(slot, night, nextDay));

        service.recalculate(plannerId);
        captureCalculation().run();

        verify(odsay).search(128.89, 37.76, 128.87, 37.78, "강릉호텔", "오죽헌");
    }

    @Test
    void 숙소가_없으면_둘째날_첫_장소도_출발지에서_계산한다() {
        PlannerScheduleSlotEntity nextDay = new PlannerScheduleSlotEntity(planId, startDate.plusDays(1), 1, 102L);
        nextDay.setSlotId(503L);
        when(slots.findByPlanIdOrderByVisitDateAscSortOrderAsc(planId))
                .thenAnswer(ignored -> List.of(slot, nextDay));

        service.recalculate(plannerId);
        captureCalculation().run();

        verify(odsay).search(128.90, 37.75, 128.87, 37.78, "강릉역", "오죽헌");
        verify(odsay, never()).search(128.90, 37.80, 128.87, 37.78, "경포대", "오죽헌");
    }

    @Test
    void API_ERROR는_캐시하지_않고_다음_재계산에서_다시_호출한다() {
        when(odsay.search(any(Double.class), any(Double.class), any(Double.class), any(Double.class),
                any(), any()))
                .thenReturn(new OdsayTransitRouteClient.SearchResult("API_ERROR", null))
                .thenReturn(new OdsayTransitRouteClient.SearchResult("READY",
                        new PlannerScheduleResponseDto.RouteLeg("PUBLIC_TRANSIT", "강릉역", "경포대",
                                25, 5, List.of())));

        service.recalculate(plannerId);
        captureCalculation().run();
        assertThat(slot.getRouteStatus()).isEqualTo("API_ERROR");

        service.recalculate(plannerId);
        captureCalculation().run();

        verify(odsay, org.mockito.Mockito.times(2)).search(any(Double.class), any(Double.class),
                any(Double.class), any(Double.class), any(), any());
        assertThat(slot.getRouteStatus()).isEqualTo("READY");
    }

    @Test
    void API_키가_없으면_경로_작업을_호출하지_않고_대기_상태를_저장한다() {
        when(odsay.isConfigured()).thenReturn(false);

        service.recalculate(plannerId);

        assertThat(slot.getRouteStatus()).isEqualTo("WAITING_FOR_API_KEY");
        verify(taskRunner, never()).submit(any());
        verify(odsay, never()).search(any(Double.class), any(Double.class), any(Double.class),
                any(Double.class), any(), any());
    }

    @Test
    void 일정이_계산중_변경되면_이전_경로로_덮어쓰지_않는다() {
        service.recalculate(plannerId);
        Runnable oldCalculation = captureCalculation();

        slot.setTourPlaceId(102L);
        service.recalculate(plannerId);
        Runnable currentCalculation = captureCalculation();

        oldCalculation.run();
        assertThat(slot.getRouteStatus()).isEqualTo("CALCULATING");
        currentCalculation.run();

        verify(odsay).search(128.90, 37.75, 128.87, 37.78, "강릉역", "오죽헌");
        assertThat(slot.getRouteStatus()).isEqualTo("READY");
    }

    private Runnable captureCalculation() {
        ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
        verify(taskRunner, org.mockito.Mockito.atLeastOnce()).submit(captor.capture());
        List<Runnable> calculations = captor.getAllValues();
        Runnable calculation = calculations.get(calculations.size() - 1);
        clearInvocations(taskRunner);
        return calculation;
    }

    private TourPlaceEntity place(Long id, String name, double latitude, double longitude) {
        TourPlaceEntity place = new TourPlaceEntity();
        place.setTourPlaceId(id);
        place.setPlaceName(name);
        place.setLatitude(latitude);
        place.setLongitude(longitude);
        return place;
    }
}
