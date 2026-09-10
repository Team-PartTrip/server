package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.request.PlannerCityRequestDto;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.PlannerCityEntity;
import com.example.PartTrip.planner.repository.PlannerCityRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 도시별 기간은 여행 기간을 빈틈 없이 이어 덮어야 한다.
 * 하루라도 비면 AI 가 그날 어느 도시에서 일정을 짤지 알 수 없다.
 */
class PlannerCityRangeTest {

    private final PlannerCityRepository repository =
            Mockito.mock(PlannerCityRepository.class);
    private final PlannerService service =
            new PlannerService(null, null, null, repository, null, null);

    private static GroupTravelPlanEntity plan() {
        GroupTravelPlanEntity plan = new GroupTravelPlanEntity();
        plan.setPlanId(1L);
        plan.setStartDate(LocalDate.of(2026, 8, 23));
        plan.setEndDate(LocalDate.of(2026, 8, 27));
        return plan;
    }

    private static PlannerCityRequestDto city(String name, String from, String to) {
        PlannerCityRequestDto dto = new PlannerCityRequestDto();
        dto.setCountryName("일본");
        dto.setCityName(name);
        dto.setStartDate(LocalDate.parse(from));
        dto.setEndDate(LocalDate.parse(to));
        return dto;
    }

    private void save(List<PlannerCityRequestDto> cities) {
        ReflectionTestUtils.invokeMethod(service, "saveCities", plan(), cities);
    }

    @Test
    @DisplayName("이어지는 두 도시는 순서대로 저장된다")
    void savesContiguousCities() {
        save(List.of(city("오사카", "2026-08-23", "2026-08-25"),
                     city("교토", "2026-08-26", "2026-08-27")));

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<PlannerCityEntity>> captor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(repository).saveAll(captor.capture());

        assertThat(captor.getValue())
                .extracting(PlannerCityEntity::getSeq, PlannerCityEntity::getCityName)
                .containsExactly(Tuple.tuple(0, "오사카"), Tuple.tuple(1, "교토"));
    }

    @Test
    @DisplayName("하루가 비면 막는다")
    void rejectsGap() {
        assertThatThrownBy(() -> save(List.of(
                city("오사카", "2026-08-23", "2026-08-24"),
                city("교토", "2026-08-26", "2026-08-27"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이어지지 않습니다");
    }

    @Test
    @DisplayName("여행 기간을 다 안 덮으면 막는다")
    void rejectsShortCoverage() {
        assertThatThrownBy(() -> save(List.of(city("오사카", "2026-08-23", "2026-08-25"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("다 덮지 않습니다");
    }

    @Test
    @DisplayName("도시 자체의 종료일이 시작일보다 빠르면 막는다")
    void rejectsReversedRange() {
        assertThatThrownBy(() -> save(List.of(city("오사카", "2026-08-25", "2026-08-23"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빠릅니다");
    }
}
