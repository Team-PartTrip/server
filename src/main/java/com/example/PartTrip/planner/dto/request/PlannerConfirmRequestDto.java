package com.example.PartTrip.planner.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 일정 확정 요청 (API-005-09).
 *
 * 장바구니에서 직접 고르거나 랜덤으로 뽑은 결과를 함께 보낸다.
 * 지정하지 않은 투표는 지금까지처럼 득표순으로 확정한다.
 * 본문 전체를 생략해도 된다. 그러면 모두 득표순이다.
 */
@Getter
@Setter
public class PlannerConfirmRequestDto {

    private List<VoteSelection> selections;

    @Getter
    @Setter
    public static class VoteSelection {

        private Long voteId;

        private Long optionId;
    }
}
