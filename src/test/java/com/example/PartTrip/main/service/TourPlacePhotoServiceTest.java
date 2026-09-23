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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/** 관광지 사진 (#170). 구글 사진 주소는 잠깐만 유효해서 이름을 두고 볼 때 받는다 */
@ExtendWith(MockitoExtension.class)
class TourPlacePhotoServiceTest {

    private static final ObjectMapper JSON = new ObjectMapper();

    @Mock TourPlaceRepository tourPlaceRepository;
    @Spy @InjectMocks TourPlacePhotoService service;

    private TourPlaceEntity place(Long id, String photoName) {
        TourPlaceEntity place = new TourPlaceEntity();
        place.setTourPlaceId(id);
        place.setPlaceName("장소" + id);
        place.setPhotoName(photoName);
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
    void 사진_이름이_있는_곳에만_우리_주소를_붙이고_구글은_부르지_않는다() {
        TourPlaceEntity withPhoto = place(7L, "places/a/photos/b");
        TourPlaceEntity without = place(8L, null);
        given(tourPlaceRepository.saveAll(any())).willAnswer(inv -> inv.getArgument(0));

        service.attachPhotos(List.of(withPhoto, without));

        assertThat(withPhoto.getImageUrl()).isEqualTo("/api/main/tour-place/7/photo");
        assertThat(without.getImageUrl()).isNull();
        verify(service, never()).resolve(any());
    }

    @Test
    void 볼_때_구글_주소를_받고_잠시_기억한다() {
        given(tourPlaceRepository.findById(7L)).willReturn(Optional.of(place(7L, "places/a/photos/b")));
        willReturn("https://photo/new").given(service).resolve("places/a/photos/b");

        assertThat(service.currentUri(7L)).contains("https://photo/new");
        assertThat(service.currentUri(7L)).contains("https://photo/new");

        // 두 번째는 기억한 주소를 쓴다. 목록을 열 때마다 구글을 부르면 돈이 나간다
        verify(service, times(1)).resolve("places/a/photos/b");
    }

    @Test
    void 사진이_없거나_못_받으면_비운다() {
        given(tourPlaceRepository.findById(8L)).willReturn(Optional.of(place(8L, null)));
        given(tourPlaceRepository.findById(9L)).willReturn(Optional.empty());
        given(tourPlaceRepository.findById(10L)).willReturn(Optional.of(place(10L, "p")));
        willReturn(null).given(service).resolve("p");

        assertThat(service.currentUri(8L)).isEmpty();
        assertThat(service.currentUri(9L)).isEmpty();
        assertThat(service.currentUri(10L)).isEmpty();
    }
}
