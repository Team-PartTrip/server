package com.example.PartTrip.region.service;

import com.example.PartTrip.region.dto.response.RegionMapResponseDto;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.entity.TripCardPhotoEntity;
import com.example.PartTrip.tripcard.entity.TripCardPlaceEntity;
import jakarta.persistence.EntityManager;
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
    @Autowired private EntityManager em;

    @Test
    void 같은_시도를_두_번_가면_한_줄에_두_번으로_센다() {
        save("traveler", "51", "강릉시");
        save("traveler", "51", "속초시");
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
                        org.assertj.core.groups.Tuple.tuple("50", "제주특별자치도", 1L),
                        org.assertj.core.groups.Tuple.tuple("51", "강원특별자치도", 2L));
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

    @Test
    void 카드마다_장소와_사진_좌표를_중복_없이_준다() {
        Long tongyeong = save("traveler", "48", "통영");
        point(tongyeong, 34.85441, 128.43321, true);
        point(tongyeong, 34.85442, 128.43322, false); // 같은 곳에서 찍은 사진
        point(tongyeong, 34.8402, 128.4181, false);
        Long seoul = save("traveler", "11", "서울특별시");
        save("traveler", null, "오사카");
        point(save("other", "22", "대구"), 35.88, 128.58, true);

        RegionMapResponseDto map = regionMapService.getRegionMap("traveler");

        assertThat(map.getTrips())
                .extracting(RegionMapResponseDto.TripResponseDto::getTripCardId)
                .containsExactlyInAnyOrder(tongyeong, seoul);
        RegionMapResponseDto.TripResponseDto t = map.getTrips().stream()
                .filter(x -> x.getTripCardId().equals(tongyeong)).findFirst().orElseThrow();
        assertThat(t.getCityName()).isEqualTo("통영");
        assertThat(t.getPoints()).containsExactlyInAnyOrder(
                new double[]{34.8544, 128.4332}, new double[]{34.8402, 128.4181});
    }

    private void point(Long cardId, double lat, double lng, boolean place) {
        if (place) {
            TripCardPlaceEntity p = new TripCardPlaceEntity();
            p.setTripCardId(cardId);
            p.setPlaceName("장소");
            p.setVisitedDate(LocalDate.now().minusDays(2));
            p.setLatitude(lat);
            p.setLongitude(lng);
            em.persist(p);
        } else {
            TripCardPhotoEntity p = new TripCardPhotoEntity();
            p.setTripCardId(cardId);
            p.setImageUrl("/uploads/trip-card/" + lat + ".jpg");
            p.setLatitude(lat);
            p.setLongitude(lng);
            em.persist(p);
        }
    }

    private Long save(String userId, String regionCode, String cityName) {
        return tripCardRepository.save(TripCardEntity.builder()
                .userId(userId)
                .title(cityName + " 여행")
                .regionCode(regionCode)
                .cityName(cityName)
                .startDate(LocalDate.now().minusDays(3))
                .endDate(LocalDate.now().minusDays(1))
                .dateOver(true)
                .createdAt(LocalDateTime.now())
                .build()).getTripCardId();
    }
}
