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
    void 기차_구간을_단계로_남기고_먼_구간은_도시간_경로를_고른다() throws Exception {
        JsonNode response = objectMapper.readTree("""
                {
                  "result": {
                    "path": [
                      {"info": {"totalTime": 150}, "subPath": [
                        {"trafficType": 2, "sectionTime": 150, "startName": "반월당", "endName": "경주",
                         "lane": [{"busNo": "999"}]}
                      ]},
                      {"info": {"totalTime": 60}, "subPath": [
                        {"trafficType": 4, "sectionTime": 40, "startName": "동대구", "endName": "경주"},
                        {"trafficType": 3, "sectionTime": 20}
                      ]}
                    ]
                  }
                }
                """);

        assertThat(OdsayTransitRouteClient.parse(response, "대구", "경주", false)
                .durationMinutes()).isEqualTo(150);

        PlannerScheduleResponseDto.RouteLeg route =
                OdsayTransitRouteClient.parse(response, "대구", "경주", true);
        assertThat(route.durationMinutes()).isEqualTo(60);
        assertThat(route.steps().get(0).type()).isEqualTo("TRAIN");
        assertThat(route.steps().get(0).boardingStop()).isEqualTo("동대구");
        assertThat(route.steps().get(0).alightingStop()).isEqualTo("경주");
    }

    @Test
    void 칠백미터_안은_호출없이_걸어서_가는_경로로_둔다() {
        OdsayTransitRouteClient client = new OdsayTransitRouteClient("test-key");

        OdsayTransitRouteClient.SearchResult result =
                client.search(128.5877, 35.8697, 128.5812, 35.8691, "청라언덕", "서문시장");

        assertThat(result.status()).isEqualTo("READY");
        assertThat(result.route().transportMode()).isEqualTo("WALKING");
        assertThat(result.route().durationMinutes()).isEqualTo(12);
    }

    @Test
    void 역까지_가는_시내_구간을_기차_앞뒤에_붙인다() {
        var step = (java.util.function.BiFunction<String, Integer, PlannerScheduleResponseDto.RouteStep>)
                (type, minutes) -> new PlannerScheduleResponseDto.RouteStep(type, null, null, null, null, minutes);
        var head = new PlannerScheduleResponseDto.RouteLeg("PUBLIC_TRANSIT", "집", "서울",
                20, 5, java.util.List.of(step.apply("WALK", 5), step.apply("SUBWAY", 15)));
        var train = new PlannerScheduleResponseDto.RouteLeg("PUBLIC_TRANSIT", "집", "월정교",
                117, 0, java.util.List.of(step.apply("TRAIN", 117)));
        var tail = new PlannerScheduleResponseDto.RouteLeg("PUBLIC_TRANSIT", "경주", "월정교",
                30, 8, java.util.List.of(step.apply("BUS", 22), step.apply("WALK", 8)));

        var joined = OdsayTransitRouteClient.join(train, head, tail);

        assertThat(joined.durationMinutes()).isEqualTo(167);
        assertThat(joined.walkingMinutes()).isEqualTo(13);
        assertThat(joined.fromName()).isEqualTo("집");
        assertThat(joined.steps()).extracting(PlannerScheduleResponseDto.RouteStep::type)
                .containsExactly("WALK", "SUBWAY", "TRAIN", "BUS", "WALK");
        // 한쪽을 못 찾으면 있는 쪽만 붙인다
        assertThat(OdsayTransitRouteClient.join(train, null, tail).steps()).hasSize(3);
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
