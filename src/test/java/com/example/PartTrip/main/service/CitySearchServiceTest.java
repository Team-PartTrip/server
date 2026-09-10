package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.CitySearchResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CitySearchServiceTest {

    private final CitySearchService service = new CitySearchService();
    private final ObjectMapper mapper = new ObjectMapper();

    private JsonNode json(String raw) throws Exception {
        return mapper.readTree(raw);
    }

    @Test
    @DisplayName("도시 이름과 나라를 뽑는다. 요청한 나라를 그대로 쓴다")
    void parse() throws Exception {
        JsonNode body = json("""
                {"suggestions":[
                  {"placePrediction":{"structuredFormat":{
                    "mainText":{"text":"오사카"},
                    "secondaryText":{"text":"일본 오사카부"}}}},
                  {"placePrediction":{"structuredFormat":{
                    "mainText":{"text":"교토"},
                    "secondaryText":{"text":"일본 교토부"}}}}]}
                """);

        List<CitySearchResponseDto> cities = service.parse(body, "일본");

        assertThat(cities).hasSize(2);
        assertThat(cities.get(0).getCityName()).isEqualTo("오사카");
        assertThat(cities.get(0).getCountryName()).isEqualTo("일본");
        assertThat(cities.get(1).getCityName()).isEqualTo("교토");
    }

    @Test
    @DisplayName("같은 도시가 두 번 오면 하나만 남긴다")
    void parseDeduplicates() throws Exception {
        JsonNode body = json("""
                {"suggestions":[
                  {"placePrediction":{"structuredFormat":{"mainText":{"text":"오사카"}}}},
                  {"placePrediction":{"structuredFormat":{"mainText":{"text":"오사카"}}}}]}
                """);

        assertThat(service.parse(body, "일본")).hasSize(1);
    }

    @Test
    @DisplayName("secondaryText 가 없으면 요청한 나라를 그대로 쓴다")
    void parseFallsBackToRequestedCountry() throws Exception {
        JsonNode body = json("""
                {"suggestions":[
                  {"placePrediction":{"structuredFormat":{"mainText":{"text":"오사카"}}}}]}
                """);

        assertThat(service.parse(body, "일본").get(0).getCountryName()).isEqualTo("일본");
    }

    @Test
    @DisplayName("나라 없이 검색하면 secondaryText 의 첫 조각이 나라다")
    void parseWithoutRequestedCountry() throws Exception {
        // 한국어 응답은 "일본 오사카부", "태국 치앙마이 …" 처럼 나라가 앞에 온다
        JsonNode body = json("""
                {"suggestions":[
                  {"placePrediction":{"structuredFormat":{
                    "mainText":{"text":"오사카시"},
                    "secondaryText":{"text":"일본 오사카부"}}}},
                  {"placePrediction":{"structuredFormat":{
                    "mainText":{"text":"치앙마이"},
                    "secondaryText":{"text":"태국 치앙마이 Mueang Chiang Mai District"}}}}]}
                """);

        List<CitySearchResponseDto> cities = service.parse(body, null);

        assertThat(cities.get(0).getCountryName()).isEqualTo("일본");
        assertThat(cities.get(1).getCountryName()).isEqualTo("태국");
    }

    @Test
    @DisplayName("결과가 없거나 응답이 비면 빈 목록이다")
    void parseEmpty() throws Exception {
        assertThat(service.parse(json("{}"), "일본")).isEmpty();
        assertThat(service.parse(null, "일본")).isEmpty();
    }

    @Test
    @DisplayName("한 글자 이하로는 구글을 부르지 않는다. 부르면 API 키가 없어 터진다")
    void shortKeywordDoesNotCallGoogle() {
        assertThat(service.search("일본", "오")).isEmpty();
        assertThat(service.search("일본", " ")).isEmpty();
        assertThat(service.search("일본", null)).isEmpty();
    }
}
