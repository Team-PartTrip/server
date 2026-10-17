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
                  {"place_name":"VAVAS MALL"},
                  {"place_name":"바바스몰 "},
                  {"place_name":"바바스 주차장"}
                ]}""");

        assertThat(KakaoPlaceNameService.pick(body)).isEqualTo("바바스몰");
    }

    @Test
    void 한글_결과가_없으면_null() throws Exception {
        var body = new ObjectMapper().readTree("{\"documents\":[{\"place_name\":\"VAVAS MALL\"}]}");

        assertThat(KakaoPlaceNameService.pick(body)).isNull();
        assertThat(KakaoPlaceNameService.pick(new ObjectMapper().readTree("{\"documents\":[]}"))).isNull();
    }

    @Test
    void 이미_한국어_이름이거나_키_좌표가_없으면_카카오를_부르지_않는다() {
        var service = new KakaoPlaceNameService("");

        assertThat(service.koreanName("메가박스", 37.6, 127.2)).isNull();
        assertThat(service.koreanName("VAVAS MALL", 37.6, 127.2)).isNull();
        assertThat(new KakaoPlaceNameService("key").koreanName("VAVAS MALL", null, null)).isNull();
    }
}
