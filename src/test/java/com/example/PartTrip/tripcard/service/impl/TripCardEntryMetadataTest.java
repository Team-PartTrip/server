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

import java.time.LocalDate;
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

    @Test
    void 여행_기간_밖의_촬영_시각은_거부한다() {
        // 사진을 읽기도 전에 걸러진다
        given(currentUserProvider.getCurrentUserId()).willReturn("member");
        given(tripCardRepository.findByTripCardIdAndUserId(1L, "member"))
                .willReturn(Optional.of(closedCard()));

        assertThatThrownBy(() -> service.updateMetadata(1L, 10L,
                request(null, null, null, LocalDateTime.of(2019, 5, 1, 9, 0))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("여행 기간");
    }

    // 앞으로 갈 여행의 카드에도 사진을 붙일 수 있어서, 기간만 보면 아직 오지 않은
    // 날짜가 통과한다. 2099년 여행 카드로 그 경우를 만든다.
    @Test
    void 아직_오지_않은_날짜는_여행_기간_안이어도_거부한다() {
        given(currentUserProvider.getCurrentUserId()).willReturn("member");
        given(tripCardRepository.findByTripCardIdAndUserId(1L, "member"))
                .willReturn(Optional.of(TripCardEntity.builder()
                        .tripCardId(1L).userId("member")
                        .startDate(LocalDate.of(2099, 1, 1))
                        .endDate(LocalDate.of(2099, 1, 3))
                        .build()));

        assertThatThrownBy(() -> service.updateMetadata(1L, 10L,
                request(null, null, null, LocalDateTime.of(2099, 1, 2, 9, 0))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("아직 오지 않은");
    }

    // 여행 카드는 그 여행의 기록이다. 올릴 때도, 직접 넣을 때도 같은 자를 쓴다.
    @Test
    void 여행_첫날과_마지막날은_기간_안이다() {
        LocalDate start = LocalDate.of(2026, 8, 14);
        LocalDate end = LocalDate.of(2026, 8, 17);

        assertThat(TripCardEntryServiceImpl.withinTripDates(start, end,
                LocalDateTime.of(2026, 8, 14, 0, 0))).isTrue();
        assertThat(TripCardEntryServiceImpl.withinTripDates(start, end,
                LocalDateTime.of(2026, 8, 17, 23, 59))).isTrue();
        assertThat(TripCardEntryServiceImpl.withinTripDates(start, end,
                LocalDateTime.of(2026, 8, 13, 23, 59))).isFalse();
        assertThat(TripCardEntryServiceImpl.withinTripDates(start, end,
                LocalDateTime.of(2026, 8, 18, 0, 0))).isFalse();
    }

    @Test
    void 촬영_시각을_모르는_사진은_기간_검사를_통과한다() {
        // 카카오톡으로 받은 사진이 이 경우다. 여기서 막으면 Func-003-07 자체가 쓸모없어진다.
        assertThat(TripCardEntryServiceImpl.withinTripDates(
                LocalDate.of(2026, 8, 14), LocalDate.of(2026, 8, 17), null)).isTrue();
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

    /** 2026-08-14 ~ 08-17 로 이미 끝난 카드. 다른 수정은 막히지만 촬영 정보 지정은 열려 있어야 한다. */
    private TripCardEntity closedCard() {
        return TripCardEntity.builder()
                .tripCardId(1L)
                .userId("member")
                .startDate(LocalDate.of(2026, 8, 14))
                .endDate(LocalDate.of(2026, 8, 17))
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
