package com.example.PartTrip.tripcard.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.PartTrip.tripcard.entity.TripCardEntity;
import org.junit.jupiter.api.Test;

class TripCardResponseTest {

    @Test
    void 카드_제목을_응답에_담는다() {
        TripCardEntity card = TripCardEntity.builder()
                .tripCardId(7L)
                .title("대구 가을 나들이")
                .build();

        TripCardResponse response = TripCardResponse.from(card);

        assertThat(response.getCardId()).isEqualTo(7L);
        assertThat(response.getTitle()).isEqualTo("대구 가을 나들이");
    }
}
