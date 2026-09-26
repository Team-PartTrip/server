package com.example.PartTrip.tripcard.service.impl;

import com.example.PartTrip.global.security.CurrentUserProvider;
import com.example.PartTrip.global.storage.ImageStorageService;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.tripcard.dto.response.TimelineItemResponse;
import com.example.PartTrip.tripcard.entity.MetadataSource;
import com.example.PartTrip.tripcard.entity.TimelineItemType;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.entity.TripCardPhotoEntity;
import com.example.PartTrip.tripcard.repository.TripCardPhotoRepository;
import com.example.PartTrip.tripcard.repository.TripCardPlaceRepository;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

// 위치를 채운 사진은 위치 없는 사진이 아니라 일반 사진으로 나가야 한다 (이슈 #147 완료 조건).
// 앱이 "위치 지정" 버튼을 다시 띄울 수 있도록 출처도 함께 실어 보낸다.
@ExtendWith(MockitoExtension.class)
class TripCardTimelinePhotoTest {

    @Mock private TripCardRepository tripCardRepository;
    @Mock private CurrentUserProvider currentUserProvider;
    @Mock private TripCardPlaceRepository tripCardPlaceRepository;
    @Mock private TripCardPhotoRepository tripCardPhotoRepository;
    @Mock private TourPlaceRepository tourPlaceRepository;
    @Mock private ImageStorageService imageStorageService;
    @InjectMocks private TripCardServiceImpl service;

    @Test
    void 직접_고른_위치가_있으면_일반_사진으로_나간다() {
        TripCardPhotoEntity photo = photo();
        photo.setLatitude(33.45);
        photo.setLongitude(126.94);
        photo.setPlaceName("성산일출봉");
        photo.setLocationSource(MetadataSource.MANUAL);
        givenCardWith(photo);

        TimelineItemResponse item = service.getTripCard(1L).getTimeline().get(0);

        assertThat(item.getType()).isEqualTo(TimelineItemType.PHOTO);
        assertThat(item.getPlaceName()).isEqualTo("성산일출봉");
        assertThat(item.getLocationSource()).isEqualTo(MetadataSource.MANUAL);
    }

    @Test
    void 위치가_없으면_위치_없는_사진으로_나간다() {
        givenCardWith(photo());

        TimelineItemResponse item = service.getTripCard(1L).getTimeline().get(0);

        assertThat(item.getType()).isEqualTo(TimelineItemType.NO_INFO_PHOTO);
        assertThat(item.getLocationSource()).isNull();
    }

    private void givenCardWith(TripCardPhotoEntity photo) {
        given(currentUserProvider.getCurrentUserId()).willReturn("member");
        given(tripCardRepository.findByTripCardIdAndUserId(1L, "member"))
                .willReturn(Optional.of(TripCardEntity.builder()
                        .tripCardId(1L).userId("member").build()));
        given(tripCardPlaceRepository.findByTripCardIdOrderByVisitedDateAscSortOrderAsc(1L))
                .willReturn(List.of());
        given(tripCardPhotoRepository.findByTripCardIdOrderByTakenAtAsc(1L))
                .willReturn(List.of(photo));
        given(tourPlaceRepository.findAllById(List.of())).willReturn(List.of());
    }

    private TripCardPhotoEntity photo() {
        TripCardPhotoEntity photo = new TripCardPhotoEntity();
        photo.setTripCardPhotoId(10L);
        photo.setTripCardId(1L);
        photo.setImageUrl("/uploads/trip-card/1/10.jpg");
        return photo;
    }
}
