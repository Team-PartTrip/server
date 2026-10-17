package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.CountryInfoResponseDto;
import com.example.PartTrip.main.entity.CountryInfoEntity;
import com.example.PartTrip.main.repository.CountryInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * 여행지 검색은 도시 이름만 본다.
 *
 * 예전에는 나라 이름으로도 찾고 ISO 전체 국가를 얹어서 줬다. 그때는 도시가
 * 없는 줄이 섞여 나왔고(예: "대한민국"), 그걸 고르면 나라 이름이 도시 자리에
 * 저장돼 관광지를 하나도 못 받아왔다. 나라 안의 도시는 /api/main/cities 가
 * 구글에서 찾아준다.
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
                country(1L, "일본", "도쿄"),
                country(2L, "프랑스", "파리"),
                country(3L, "한국", "서울"),
                country(4L, "아이슬란드", null)));
    }

    private CountryInfoEntity country(Long id, String name, String city) {
        CountryInfoEntity e = new CountryInfoEntity();
        e.setCountryInfoId(id);
        e.setCountryName(name);
        e.setCityName(city);
        return e;
    }

    private List<String> citiesFor(String keyword) {
        return countryInfoService.getCountries(keyword).stream()
                .map(CountryInfoResponseDto::getCityName)
                .toList();
    }

    @Test
    @DisplayName("도시 이름으로 찾는다")
    void findsByCityName() {
        assertThat(citiesFor("파리")).containsExactly("파리");
    }

    @Test
    @DisplayName("나라 이름으로는 안 걸린다. 도시 없는 줄을 고르면 관광지를 못 받아온다")
    void doesNotFindByCountryName() {
        assertThat(citiesFor("일본")).isEmpty();
        assertThat(citiesFor("대한민국")).isEmpty();
    }

    @Test
    @DisplayName("도시가 비어 있는 여행지는 아예 안 나온다")
    void skipsRowsWithoutCity() {
        assertThat(citiesFor("")).doesNotContainNull().containsExactly("도쿄", "서울", "파리");
    }

    @Test
    @DisplayName("검색어가 없으면 담아둔 여행지를 전부 준다")
    void returnsAllWhenKeywordBlank() {
        assertThat(citiesFor("")).hasSize(3);
    }
}
