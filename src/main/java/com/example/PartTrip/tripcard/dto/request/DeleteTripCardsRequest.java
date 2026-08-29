package com.example.PartTrip.tripcard.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

// 명세서 API-003-05 의 요청 본문은 { cardIds: [Long] } 이다.
// 배열을 그대로 받으면 앱이 명세서대로 보냈을 때 400 이 난다.
@Getter
@Setter
public class DeleteTripCardsRequest {

    @NotEmpty(message = "삭제할 카드를 선택해주세요.")
    private Set<@NotNull(message = "카드 id 는 비어 있을 수 없습니다.") Long> cardIds;
}
