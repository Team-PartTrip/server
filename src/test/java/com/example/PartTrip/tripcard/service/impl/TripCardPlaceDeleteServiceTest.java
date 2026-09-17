package com.example.PartTrip.tripcard.service.impl;

import com.example.PartTrip.global.security.CurrentUserProvider;
import com.example.PartTrip.global.storage.ImageStorageService;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.entity.TripCardPlaceEntity;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TripCardPlaceDeleteServiceTest {

    @Mock private TripCardRepository tripCardRepository;
    @Mock private TripCardPhotoRepository tripCardPhotoRepository;
    @Mock private TripCardPlaceRepository tripCardPlaceRepository;
    @Mock private CurrentUserProvider currentUserProvider;
    @Mock private ImageStorageService imageStorageService;
    @InjectMocks private TripCardEntryServiceImpl service;

    @Test
    void 자신의_카드에_속한_장소를_삭제한다() {
        TripCardEntity card = editableCard(3);
        TripCardPlaceEntity place = place(11L, 1L, 100L);
        given(currentUserProvider.getCurrentUserId()).willReturn("member");
        given(tripCardRepository.findByTripCardIdAndUserId(1L, "member"))
                .willReturn(Optional.of(card));
        given(tripCardPlaceRepository.findById(11L)).willReturn(Optional.of(place));
        given(tripCardPlaceRepository.findByTripCardIdOrderByVisitedDateAscSortOrderAsc(1L))
                .willReturn(List.of());

        service.deletePlace(1L, 11L);

        verify(tripCardPlaceRepository).delete(place);
        assertThat(card.getPlaceCount()).isEqualTo(2);
    }

    @Test
    void 같은_장소가_다른_날에도_남아있으면_장소_수는_줄이지_않는다() {
        TripCardEntity card = editableCard(3);
        TripCardPlaceEntity deleted = place(11L, 1L, 100L);
        TripCardPlaceEntity remaining = place(12L, 1L, 100L);
        given(currentUserProvider.getCurrentUserId()).willReturn("member");
        given(tripCardRepository.findByTripCardIdAndUserId(1L, "member"))
                .willReturn(Optional.of(card));
        given(tripCardPlaceRepository.findById(11L)).willReturn(Optional.of(deleted));
        given(tripCardPlaceRepository.findByTripCardIdOrderByVisitedDateAscSortOrderAsc(1L))
                .willReturn(List.of(remaining));

        service.deletePlace(1L, 11L);

        assertThat(card.getPlaceCount()).isEqualTo(3);
    }

    @Test
    void 다른_카드의_장소는_삭제하지_않는다() {
        TripCardEntity card = editableCard(3);
        TripCardPlaceEntity place = place(11L, 2L, 100L);
        given(currentUserProvider.getCurrentUserId()).willReturn("member");
        given(tripCardRepository.findByTripCardIdAndUserId(1L, "member"))
                .willReturn(Optional.of(card));
        given(tripCardPlaceRepository.findById(11L)).willReturn(Optional.of(place));

        assertThatThrownBy(() -> service.deletePlace(1L, 11L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 카드에 속한 장소 항목이 아닙니다");
    }

    private TripCardEntity editableCard(int placeCount) {
        return TripCardEntity.builder()
                .tripCardId(1L)
                .userId("member")
                .placeCount(placeCount)
                .dateOver(false)
                .build();
    }

    private TripCardPlaceEntity place(Long id, Long cardId, Long tourPlaceId) {
        TripCardPlaceEntity place = new TripCardPlaceEntity();
        place.setTripCardPlaceId(id);
        place.setTripCardId(cardId);
        place.setTourPlaceId(tourPlaceId);
        return place;
    }
}
