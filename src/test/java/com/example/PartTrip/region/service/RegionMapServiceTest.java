package com.example.PartTrip.region.service;

import com.example.PartTrip.region.dto.response.RegionMapResponseDto;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(RegionMapService.class)
class RegionMapServiceTest {

    @Autowired private RegionMapService regionMapService;
    @Autowired private TripCardRepository tripCardRepository;

    @Test
    void 같은_시도를_두_번_가면_한_줄에_두_번으로_센다() {
        save("traveler", "42", "강릉시");
        save("traveler", "42", "속초시");
        save("traveler", "50", "서귀포시");
        // 남의 카드는 세지 않는다
        save("other", "11", "서울특별시");

        RegionMapResponseDto map = regionMapService.getRegionMap("traveler");

        assertThat(map.getTotalRegions()).isEqualTo(17);
        assertThat(map.getVisited())
                .extracting(
                        RegionMapResponseDto.VisitedRegionResponseDto::getRegionCode,
                        RegionMapResponseDto.VisitedRegionResponseDto::getRegionName,
                        RegionMapResponseDto.VisitedRegionResponseDto::getTripCount)
                .containsExactly(
                        org.assertj.core.groups.Tuple.tuple("42", "강원특별자치도", 2L),
                        org.assertj.core.groups.Tuple.tuple("50", "제주특별자치도", 1L));
    }

    @Test
    void 지역이_없는_예전_해외_카드는_지도에서_빠진다() {
        save("traveler", null, "오사카");
        save("traveler", "11", "서울특별시");

        RegionMapResponseDto map = regionMapService.getRegionMap("traveler");

        assertThat(map.getVisited())
                .extracting(RegionMapResponseDto.VisitedRegionResponseDto::getRegionCode)
                .containsExactly("11");
    }

    private void save(String userId, String regionCode, String cityName) {
        tripCardRepository.save(TripCardEntity.builder()
                .userId(userId)
                .title(cityName + " 여행")
                .regionCode(regionCode)
                .cityName(cityName)
                .startDate(LocalDate.now().minusDays(3))
                .endDate(LocalDate.now().minusDays(1))
                .dateOver(true)
                .createdAt(LocalDateTime.now())
                .build());
    }
}
