package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.PlaceSearchResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PlaceSearchServiceTest {

    @Test
    void 이름과_좌표가_있는_장소만_돌려준다() throws Exception {
        var body = new ObjectMapper().readTree("""
                {"places":[
                  {"displayName":{"text":"동대구역"},"formattedAddress":"대구 동구 동대구로 550","location":{"latitude":35.8793,"longitude":128.6286}},
                  {"displayName":{"text":"좌표 없는 곳"},"formattedAddress":"어딘가"},
                  {"formattedAddress":"이름 없는 곳","location":{"latitude":1,"longitude":2}}
                ]}""");

        assertThat(PlaceSearchService.parse(body)).containsExactly(
                new PlaceSearchResponseDto("동대구역", "대구 동구 동대구로 550", 35.8793, 128.6286));
    }

    @Test
    void 빈_검색어는_구글을_부르지_않고_빈_목록이다() {
        assertThat(new PlaceSearchService().search("  ")).isEmpty();
    }

    @Test
    void 해외_장소는_거른다() throws Exception {
        var body = new ObjectMapper().readTree("""
                {"places":[
                  {"displayName":{"text":"도쿄역"},"formattedAddress":"일본 도쿄","location":{"latitude":35.6812,"longitude":139.7671}},
                  {"displayName":{"text":"제주공항"},"formattedAddress":"제주","location":{"latitude":33.5104,"longitude":126.4914}}
                ]}""");

        assertThat(PlaceSearchService.parse(body)).extracting(PlaceSearchResponseDto::name)
                .containsExactly("제주공항");
    }

    @Test
    void 키가_없으면_빈_목록이_아니라_503_이다() {
        assertThatThrownBy(() -> new PlaceSearchService().search("동대구역"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("503");
    }
}
