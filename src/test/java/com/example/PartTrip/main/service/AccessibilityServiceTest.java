package com.example.PartTrip.main.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AccessibilityServiceTest {

    private final ObjectMapper om = new ObjectMapper();

    private JsonNode candidate(String title, double dist) {
        return om.createObjectNode().put("contentid", title).put("title", title).put("dist", String.valueOf(dist));
    }

    @Test
    void 이름이_비슷하고_가까운_곳을_고른다() {
        List<JsonNode> near = List.of(
                candidate("국립고궁박물관", 40),
                candidate("경복궁", 120),
                candidate("경복궁 (Gyeongbokgung Palace)", 90));

        assertThat(AccessibilityService.pick("경복궁", near))
                .map(n -> n.path("dist").asDouble()).contains(90.0);
    }

    @Test
    void 이름이_달라도_아주_가까우면_고르고_멀면_짝이_없다() {
        assertThat(AccessibilityService.pick("통영 케이블카", List.of(candidate("한려수도 조망 케이블카", 20))))
                .isPresent();
        assertThat(AccessibilityService.pick("통영 케이블카", List.of(candidate("미륵산", 80))))
                .isEmpty();
        assertThat(AccessibilityService.pick("경복궁", List.of(candidate("경복궁", 700))))
                .isEmpty();
    }

    @Test
    void 비어_있지_않은_항목만_정해진_순서로_준다() {
        JsonNode detail = om.createObjectNode()
                .put("parking", "장애인 주차구역 있음")
                .put("elevator", "")
                .put("restroom", "장애인 화장실 있음(1층)")
                .put("braileblock", "점자블록 있음");

        assertThat(AccessibilityService.items(detail))
                .extracting(i -> i.key() + ":" + i.text())
                .containsExactly("RESTROOM:장애인 화장실 있음(1층)", "PARKING:장애인 주차구역 있음");
    }

    @Test
    void 이름으로_찾은_장소의_거리를_직접_잰다() {
        double m = AccessibilityService.meters(35.2303279, 128.6615089, 35.2302537352, 128.6617775588);
        assertThat(m).isBetween(20.0, 30.0);
    }
}
