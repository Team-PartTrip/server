package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.AccessibilityResponseDto;
import com.example.PartTrip.main.dto.AccessibilityResponseDto.Status;
import com.example.PartTrip.main.entity.TourPlaceAccessibilityEntity;
import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.repository.TourPlaceAccessibilityRepository;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AccessibilityStatusTest {

    private final TourPlaceRepository places = mock(TourPlaceRepository.class);
    private final TourPlaceAccessibilityRepository saved = mock(TourPlaceAccessibilityRepository.class);
    private final AccessibilityService service = new AccessibilityService(places, saved, new ObjectMapper());

    private void givenPlace() {
        TourPlaceEntity place = new TourPlaceEntity();
        place.setLatitude(35.0);
        place.setLongitude(128.0);
        given(places.findById(1L)).willReturn(Optional.of(place));
    }

    private void givenSaved(String contentId, String itemsJson) {
        TourPlaceAccessibilityEntity row = new TourPlaceAccessibilityEntity();
        row.setTourPlaceId(1L);
        row.setContentId(contentId);
        row.setItemsJson(itemsJson);
        row.setFetchedAt(LocalDateTime.now());
        given(saved.findById(1L)).willReturn(Optional.of(row));
    }

    @Test
    void 정보가_있으면_FOUND() {
        givenPlace();
        givenSaved("100", "[{\"key\":\"RESTROOM\",\"label\":\"화장실\",\"text\":\"있음\"}]");

        AccessibilityResponseDto res = service.get(1L);

        assertThat(res.status()).isEqualTo(Status.FOUND);
        assertThat(res.matched()).isTrue();
        assertThat(res.items()).hasSize(1);
    }

    @Test
    void 이미_저장된_본문의_태그도_정리해서_준다() {
        givenPlace();
        givenSaved("100", "[{\"key\":\"ROUTE\",\"label\":\"접근로\",\"text\":\"무단차<br/>지하철 연결\"}]");

        assertThat(service.get(1L).items()).extracting(i -> i.text()).containsExactly("무단차\n지하철 연결");
    }

    @Test
    void 공공데이터에_없으면_NOT_FOUND() {
        givenPlace();
        givenSaved(null, "[]");

        AccessibilityResponseDto res = service.get(1L);

        assertThat(res.status()).isEqualTo(Status.NOT_FOUND);
        assertThat(res.matched()).isFalse();
    }

    @Test
    void 찾았지만_항목이_비면_NOT_FOUND() {
        givenPlace();
        givenSaved("100", "[]");

        assertThat(service.get(1L).status()).isEqualTo(Status.NOT_FOUND);
    }

    @Test
    void 키가_없어_조회할_수_없으면_UNAVAILABLE() {
        givenPlace();
        given(saved.findById(1L)).willReturn(Optional.empty());

        assertThat(service.get(1L).status()).isEqualTo(Status.UNAVAILABLE);
    }
}
