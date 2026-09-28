package com.example.PartTrip.planner.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlannerDepartureHomeTest {

    @Test
    void 출발_장소를_안_골랐거나_집_근처면_집에서_출발한다() {
        assertThat(PlannerDraftService.usesHome(null)).isTrue();
        assertThat(PlannerDraftService.usesHome(" ")).isTrue();
        assertThat(PlannerDraftService.usesHome("집 근처")).isTrue();
    }

    @Test
    void 기차역처럼_다른_곳을_골랐으면_집_좌표를_붙이지_않는다() {
        assertThat(PlannerDraftService.usesHome("기차역")).isFalse();
        assertThat(PlannerDraftService.usesHome("버스터미널")).isFalse();
    }
}
