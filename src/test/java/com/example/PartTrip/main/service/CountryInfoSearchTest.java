package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.CountryInfoResponseDto;
import com.example.PartTrip.main.entity.CountryInfoEntity;
import com.example.PartTrip.main.repository.CountryInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * 나라 검색은 DB 여행지에 ISO 전체 국가를 얹어서 찾는다.
 *
 * 후보마다 ISO 250개국을 다시 훑던 것을 미리 만든 표로 바꿨다(성능).
 * 찾는 결과가 그대로인지 여기서 잡는다.
 */
@ExtendWith(MockitoExtension.class)
class CountryInfoSearchTest {

    @Mock
    private CountryInfoRepository countryInfoRepository;

    @InjectMocks
    private CountryInfoService countryInfoService;

    @BeforeEach
    void setUp() {
        given(countryInfoRepository.findAll()).willReturn(List.of(
                country(1L, "일본", "Tokyo"),
                country(2L, "마르티니크", "Fort-de-France")));
    }

    private CountryInfoEntity country(Long id, String name, String city) {
        CountryInfoEntity e = new CountryInfoEntity();
        e.setCountryInfoId(id);
        e.setCountryName(name);
        e.setCityName(city);
        return e;
    }

    private List<String> namesFor(String keyword) {
        return countryInfoService.getCountries(keyword).stream()
                .map(CountryInfoResponseDto::getCountryName)
                .toList();
    }

    @Test
    void 한글로_찾는다() {
        assertThat(namesFor("프랑")).contains("프랑스");
    }

    // DB 에 없는 나라도 ISO 목록에서 찾아야 한다
    @Test
    void 영문으로_쳐도_한글_이름이_나온다() {
        assertThat(namesFor("france")).contains("프랑스");
    }

    // 마르티니크의 수도가 Fort-de-France 라 도시 이름으로도 걸린다
    @Test
    void 도시_이름으로도_찾는다() {
        assertThat(namesFor("france")).contains("마르티니크");
    }

    @Test
    void DB에_있는_여행지가_먼저_나온다() {
        assertThat(namesFor("일본")).containsExactly("일본");
    }

    @Test
    void 검색어가_없으면_전체를_준다() {
        // ISO 국가 수만큼은 나온다. DB 의 둘은 중복으로 들어가지 않는다
        assertThat(namesFor("")).hasSizeGreaterThan(200);
        assertThat(namesFor("").stream().filter("일본"::equals).count()).isEqualTo(1);
    }

    @Test
    void 검색하면_스무개까지만_준다() {
        assertThat(namesFor("a")).hasSizeLessThanOrEqualTo(20);
    }
}
