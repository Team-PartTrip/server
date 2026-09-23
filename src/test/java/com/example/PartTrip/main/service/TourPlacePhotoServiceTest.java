package com.example.PartTrip.main.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/** 관광지 사진 뒤따라 채우기 (#170) */
@ExtendWith(MockitoExtension.class)
class TourPlacePhotoServiceTest {

    private static final ObjectMapper JSON = new ObjectMapper();

    @Mock TourPlaceRepository tourPlaceRepository;
    @Spy @InjectMocks TourPlacePhotoService service;

    private TourPlaceEntity place(String name, String imageUrl) {
        TourPlaceEntity place = new TourPlaceEntity();
        place.setPlaceName(name);
        place.setImageUrl(imageUrl);
        return place;
    }

    @Test
    void 검색_응답에서_첫_사진_이름을_꺼낸다() throws Exception {
        assertThat(TourPlacePhotoService.photoNameOf(
                JSON.readTree("{\"photos\":[{\"name\":\"places/a/photos/b\"},{\"name\":\"c\"}]}")))
                .isEqualTo("places/a/photos/b");
        assertThat(TourPlacePhotoService.photoNameOf(JSON.readTree("{}"))).isNull();
        assertThat(TourPlacePhotoService.photoNameOf(JSON.readTree("{\"photos\":[]}"))).isNull();
    }

    @Test
    void 사진_주소를_받아_채운다() {
        willReturn("https://photo/1").given(service).resolve("p1");
        TourPlaceEntity first = place("경포대", null);

        service.fillAsync(List.of(first), Map.of("경포대", "p1"));

        assertThat(first.getImageUrl()).isEqualTo("https://photo/1");
        verify(tourPlaceRepository).saveAll(List.of(first));
    }

    @Test
    void 한_장이_실패해도_나머지를_채운다() {
        willReturn(null).given(service).resolve("p1");
        willReturn("https://photo/2").given(service).resolve("p2");
        TourPlaceEntity failed = place("실패", null);
        TourPlaceEntity ok = place("성공", null);

        service.fillAsync(List.of(failed, ok), Map.of("실패", "p1", "성공", "p2"));

        assertThat(failed.getImageUrl()).isNull();
        assertThat(ok.getImageUrl()).isEqualTo("https://photo/2");
        verify(tourPlaceRepository).saveAll(List.of(ok));
    }

    @Test
    void 이미_사진이_있으면_다시_받지_않는다() {
        service.fillAsync(List.of(place("있음", "https://photo/old")), Map.of("있음", "p1"));

        verify(service, never()).resolve(any());
        verify(tourPlaceRepository, never()).saveAll(any());
    }

    @Test
    void 사진_이름이_하나도_없으면_아무것도_하지_않는다() {
        service.fillAsync(List.of(place("경포대", null)), Map.of());

        verify(service, never()).resolve(any());
        verify(tourPlaceRepository, never()).saveAll(any());
    }
}
