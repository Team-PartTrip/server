package com.example.PartTrip.tripcard.service.impl;

import com.example.PartTrip.global.security.CurrentUserProvider;
import com.example.PartTrip.global.storage.ImageStorageService;
import com.example.PartTrip.tripcard.dto.request.UpdateEntryMetadataRequest;
import com.example.PartTrip.tripcard.entity.MetadataSource;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

// 촬영 위치 직접 지정 (Func-003-07)
//
// 규칙 두 개를 여기서 잡는다.
//  - 사진이 원래 들고 있던 EXIF 값은 못 고친다. 기록을 바꾸는 일이기 때문이다.
//  - 비어 있던 값은 여행이 끝난 뒤에도 기한 없이 몇 번이든 고칠 수 있다.
//    카카오톡으로 받은 사진 정리는 보통 여행에서 돌아온 뒤에 한다.
@ExtendWith(MockitoExtension.class)
class TripCardEntryMetadataTest {

    @Mock private TripCardRepository tripCardRepository;
    @Mock private TripCardPhotoRepository tripCardPhotoRepository;
    @Mock private TripCardPlaceRepository tripCardPlaceRepository;
    @Mock private CurrentUserProvider currentUserProvider;
    @Mock private ImageStorageService imageStorageService;
    @InjectMocks private TripCardEntryServiceImpl service;

    @Test
    void 위치_없던_사진은_여행이_끝난_뒤에도_지정할_수_있다() {
        TripCardPhotoEntity photo = photo(10L);
        givenCard(closedCard(), photo);

        service.updateMetadata(1L, 10L, request(33.45, 126.94, "성산일출봉", null));

        assertThat(photo.getLatitude()).isEqualTo(33.45);
        assertThat(photo.getPlaceName()).isEqualTo("성산일출봉");
        assertThat(photo.getLocationSource()).isEqualTo(MetadataSource.MANUAL);
    }

    @Test
    void 직접_고른_위치는_몇_번이든_다시_고른다() {
        TripCardPhotoEntity photo = photo(10L);
        photo.setLatitude(35.15);
        photo.setLongitude(126.91);
        photo.setLocationSource(MetadataSource.MANUAL);
        givenCard(closedCard(), photo);

        service.updateMetadata(1L, 10L, request(33.45, 126.94, "성산일출봉", null));

        assertThat(photo.getLatitude()).isEqualTo(33.45);
    }

    @Test
    void 사진에_박혀_있던_EXIF_좌표는_못_고친다() {
        TripCardPhotoEntity photo = photo(10L);
        photo.setLatitude(35.15);
        photo.setLongitude(126.91);
        photo.setLocationSource(MetadataSource.EXIF);
        givenCard(closedCard(), photo);

        assertThatThrownBy(() -> service.updateMetadata(1L, 10L, request(33.45, 126.94, null, null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("촬영 위치");
        assertThat(photo.getLatitude()).isEqualTo(35.15);
    }

    @Test
    void 촬영_시각을_채우면_그_날짜_묶음의_순번을_새로_받는다() {
        TripCardPhotoEntity photo = photo(10L);
        photo.setSortOrder(1);
        TripCardPhotoEntity sameDay = photo(11L);
        sameDay.setTakenAt(LocalDateTime.of(2026, 8, 15, 9, 0));
        sameDay.setSortOrder(3);
        givenCard(closedCard(), photo);
        given(tripCardPhotoRepository.findByTripCardIdOrderByTakenAtAsc(1L))
                .willReturn(List.of(sameDay, photo));

        service.updateMetadata(1L, 10L,
                request(null, null, null, LocalDateTime.of(2026, 8, 15, 18, 0)));

        assertThat(photo.getTakenAtSource()).isEqualTo(MetadataSource.MANUAL);
        assertThat(photo.getSortOrder()).isEqualTo(4);
    }

    @Test
    void 위도만_보내면_거부한다() {
        assertThatThrownBy(() -> service.updateMetadata(1L, 10L, request(33.45, null, null, null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("함께");
    }

    @Test
    void 바꿀_값이_하나도_없으면_거부한다() {
        assertThatThrownBy(() -> service.updateMetadata(1L, 10L, request(null, null, null, null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private UpdateEntryMetadataRequest request(Double latitude, Double longitude,
                                               String placeName, LocalDateTime takenAt) {
        UpdateEntryMetadataRequest request = new UpdateEntryMetadataRequest();
        request.setLatitude(latitude);
        request.setLongitude(longitude);
        request.setPlaceName(placeName);
        request.setTakenAt(takenAt);
        return request;
    }

    private void givenCard(TripCardEntity card, TripCardPhotoEntity photo) {
        given(currentUserProvider.getCurrentUserId()).willReturn("member");
        given(tripCardRepository.findByTripCardIdAndUserId(1L, "member"))
                .willReturn(Optional.of(card));
        given(tripCardPhotoRepository.findById(photo.getTripCardPhotoId()))
                .willReturn(Optional.of(photo));
    }

    /** 여행이 이미 끝난 카드. 다른 수정은 막히지만 촬영 정보 지정은 열려 있어야 한다. */
    private TripCardEntity closedCard() {
        return TripCardEntity.builder()
                .tripCardId(1L)
                .userId("member")
                .dateOver(true)
                .build();
    }

    private TripCardPhotoEntity photo(Long id) {
        TripCardPhotoEntity photo = new TripCardPhotoEntity();
        photo.setTripCardPhotoId(id);
        photo.setTripCardId(1L);
        photo.setImageUrl("/uploads/trip-card/1/" + id + ".jpg");
        return photo;
    }
}
