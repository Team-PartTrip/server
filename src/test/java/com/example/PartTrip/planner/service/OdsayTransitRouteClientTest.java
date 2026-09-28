package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.response.PlannerScheduleResponseDto;
import com.example.PartTrip.profile.enums.PreferredTransport;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OdsayTransitRouteClientTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 버스와_도보_구간을_일정_경로로_변환한다() throws Exception {
        JsonNode response = objectMapper.readTree("""
                {
                  "result": {
                    "path": [{
                      "info": {"totalTime": 35},
                      "subPath": [
                        {"trafficType": 3, "sectionTime": 5},
                        {"trafficType": 2, "sectionTime": 20, "stationCount": 3,
                         "startName": "강릉역", "endName": "경포대", "lane": [{"busNo": "202"}]},
                        {"trafficType": 3, "sectionTime": 10}
                      ]
                    }]
                  }
                }
                """);

        PlannerScheduleResponseDto.RouteLeg route =
                OdsayTransitRouteClient.parse(response, "강릉역", "경포대");

        assertThat(route.transportMode()).isEqualTo("PUBLIC_TRANSIT");
        assertThat(route.durationMinutes()).isEqualTo(35);
        assertThat(route.walkingMinutes()).isEqualTo(15);
        assertThat(route.steps()).hasSize(3);
        assertThat(route.steps().get(1).type()).isEqualTo("BUS");
        assertThat(route.steps().get(1).name()).isEqualTo("202");
        assertThat(route.steps().get(1).boardingStop()).isEqualTo("강릉역");
        assertThat(route.steps().get(1).alightingStop()).isEqualTo("경포대");
        assertThat(route.steps().get(1).stopCount()).isEqualTo(3);
    }

    @Test
    void 경로가_없으면_null을_반환한다() throws Exception {
        JsonNode response = objectMapper.readTree("{\"result\":{\"path\":[]}}");

        assertThat(OdsayTransitRouteClient.parse(response, "출발", "도착")).isNull();
    }

    @Test
    void 자동차_경로_초를_올림해_분으로_변환한다() throws Exception {
        JsonNode response = objectMapper.readTree("{\"routes\":[{\"duration\":\"61s\"}]}");

        assertThat(GoogleDrivingRouteClient.parseDurationMinutes(response)).isEqualTo(2);
    }

    @Test
    void 자동차_경로가_없으면_null을_반환한다() throws Exception {
        JsonNode response = objectMapper.readTree("{\"routes\":[]}");

        assertThat(GoogleDrivingRouteClient.parseDurationMinutes(response)).isNull();
    }

    @Test
    void 오디세이_키가_없으면_외부_호출없이_대기상태를_반환한다() {
        OdsayTransitRouteClient client = new OdsayTransitRouteClient("");

        assertThat(client.search(127.0, 37.0, 127.1, 37.1, "출발", "도착").status())
                .isEqualTo("WAITING_FOR_API_KEY");
    }

    @Test
    void 구글_경로_키가_없으면_외부_호출없이_대기상태를_반환한다() {
        GoogleDrivingRouteClient client = new GoogleDrivingRouteClient("");

        assertThat(client.search(PreferredTransport.CAR,
                37.0, 127.0, 37.1, 127.1, "출발", "도착").status())
                .isEqualTo("WAITING_FOR_API_KEY");
    }

    @Test
    void 오디세이_하루_호출_한도를_넘기지_않는다() {
        OdsayDailyCallBudget budget = new OdsayDailyCallBudget(2);

        assertThat(budget.tryAcquire()).isTrue();
        assertThat(budget.tryAcquire()).isTrue();
        assertThat(budget.tryAcquire()).isFalse();
    }
}
