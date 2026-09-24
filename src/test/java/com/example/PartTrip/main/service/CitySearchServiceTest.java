package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.CitySearchResponseDto;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CitySearchServiceTest {

    private final TourPlaceRepository tourPlaceRepository =
            Mockito.mock(TourPlaceRepository.class);
    private final CitySearchService service = new CitySearchService(tourPlaceRepository);
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
    @DisplayName("secondaryText 두 번째 조각이 시·도다. 없으면 null")
    void parseRegionName() throws Exception {
        JsonNode body = json("""
                {"suggestions":[
                  {"placePrediction":{"structuredFormat":{
                    "mainText":{"text":"광주"},
                    "secondaryText":{"text":"대한민국 광주광역시"}}}},
                  {"placePrediction":{"structuredFormat":{
                    "mainText":{"text":"광주시"},
                    "secondaryText":{"text":"대한민국 경기도"}}}},
                  {"placePrediction":{"structuredFormat":{"mainText":{"text":"서울"}}}}]}
                """);

        List<CitySearchResponseDto> cities = service.parse(body, "한국");

        assertThat(cities).extracting(CitySearchResponseDto::getRegionName)
                .containsExactly("광주광역시", "경기도", null);
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
    @DisplayName("이미 담아둔 도시가 있으면 그 이름으로 바꾼다 - 오사카시 → 오사카")
    void reusesKnownCityName() throws Exception {
        Mockito.when(tourPlaceRepository.findCityNames("일본"))
                .thenReturn(List.of("오사카", "후쿠오카"));

        JsonNode body = json("""
                {"suggestions":[
                  {"placePrediction":{"structuredFormat":{
                    "mainText":{"text":"오사카시"},
                    "secondaryText":{"text":"일본 오사카부"}}}},
                  {"placePrediction":{"structuredFormat":{
                    "mainText":{"text":"교토시"},
                    "secondaryText":{"text":"일본 교토부"}}}}]}
                """);

        List<CitySearchResponseDto> cities = service.withKnownNames(service.parse(body, "일본"));

        assertThat(cities.get(0).getCityName()).isEqualTo("오사카");
        // 담아둔 적 없는 도시는 구글이 준 이름 그대로 둔다
        assertThat(cities.get(1).getCityName()).isEqualTo("교토시");
    }

    @Test
    @DisplayName("두 글자 이상 다르면 다른 도시로 둔다")
    void doesNotMatchDifferentCity() throws Exception {
        Mockito.when(tourPlaceRepository.findCityNames("일본")).thenReturn(List.of("오사카"));

        JsonNode body = json("""
                {"suggestions":[
                  {"placePrediction":{"structuredFormat":{
                    "mainText":{"text":"오사카사야마시"},
                    "secondaryText":{"text":"일본 오사카부"}}}}]}
                """);

        assertThat(service.withKnownNames(service.parse(body, "일본")).get(0).getCityName())
                .isEqualTo("오사카사야마시");
    }

    @Test
    @DisplayName("한 글자 이하로는 구글을 부르지 않는다. 부르면 API 키가 없어 터진다")
    void shortKeywordDoesNotCallGoogle() {
        assertThat(service.search("일본", "오")).isEmpty();
        assertThat(service.search("일본", " ")).isEmpty();
        assertThat(service.search("일본", null)).isEmpty();
    }
}
