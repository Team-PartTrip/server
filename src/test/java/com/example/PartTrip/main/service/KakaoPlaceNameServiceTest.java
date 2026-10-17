package com.example.PartTrip.main.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KakaoPlaceNameServiceTest {

    @Test
    void 한글이_하나라도_있으면_한국어_이름이다() {
        assertThat(KakaoPlaceNameService.hasHangul("메가박스 남양주")).isTrue();
        assertThat(KakaoPlaceNameService.hasHangul("CGV 용산아이파크몰")).isTrue();
        assertThat(KakaoPlaceNameService.hasHangul("VAVAS MALL")).isFalse();
        assertThat(KakaoPlaceNameService.hasHangul(null)).isFalse();
    }

    @Test
    void 가까운_순_결과_중_한글_이름을_고른다() throws Exception {
        var body = new ObjectMapper().readTree("""
                {"documents":[
                  {"place_name":"VAVAS MALL","x":"127.2735","y":"37.6543"},
                  {"place_name":"바바스몰 ","x":"127.2735","y":"37.6543"},
                  {"place_name":"바바스 주차장","x":"127.2736","y":"37.6544"}
                ]}""");

        assertThat(KakaoPlaceNameService.pick(body, 37.6543, 127.2735)).isEqualTo("바바스몰");
    }

    @Test
    void 구글_좌표에서_먼_결과는_다른_곳이라_버린다() throws Exception {
        var body = new ObjectMapper().readTree("""
                {"documents":[{"place_name":"메종에르메스도산파크 주차장","x":"127.0386","y":"37.5249"}]}""");

        assertThat(KakaoPlaceNameService.pick(body, 48.8566, 2.3522)).isNull();
    }

    @Test
    void 한글_결과가_없으면_null() throws Exception {
        var body = new ObjectMapper().readTree(
                "{\"documents\":[{\"place_name\":\"VAVAS MALL\",\"x\":\"127.27\",\"y\":\"37.65\"}]}");

        assertThat(KakaoPlaceNameService.pick(body, 37.65, 127.27)).isNull();
        assertThat(KakaoPlaceNameService.pick(new ObjectMapper().readTree("{\"documents\":[]}"), 37.65, 127.27)).isNull();
    }

    @Test
    void 이미_한국어_이름이거나_키_좌표가_없으면_카카오를_부르지_않는다() {
        var service = new KakaoPlaceNameService("");

        assertThat(service.koreanName("메가박스", 37.6, 127.2)).isNull();
        assertThat(service.koreanName("VAVAS MALL", 37.6, 127.2)).isNull();
        assertThat(new KakaoPlaceNameService("key").koreanName("VAVAS MALL", null, null)).isNull();
        // 해외 좌표는 카카오를 부르지 않는다
        assertThat(new KakaoPlaceNameService("key").koreanName("Maison Park", 48.8566, 2.3522)).isNull();
    }
}
