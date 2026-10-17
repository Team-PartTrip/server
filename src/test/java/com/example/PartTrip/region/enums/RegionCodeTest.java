package com.example.PartTrip.region.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RegionCodeTest {

    @Test
    void 이름은_모르는_코드에도_던지지_않는다() {
        // D-day · 플래너 목록 · 여행카드 응답이 이걸 그냥 부른다.
        // 던지면 코드표에 없는 값 한 줄 때문에 그 API 들이 통째로 실패한다
        assertThat(RegionCode.nameOf("51")).isEqualTo("강원특별자치도");
        assertThat(RegionCode.nameOf(null)).isNull();
        assertThat(RegionCode.nameOf("99")).isNull();
    }

    @Test
    void 값을_받을_때는_없는_코드를_막는다() {
        assertThatThrownBy(() -> RegionCode.of("99"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("시·도 코드");
    }
}
