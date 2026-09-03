package com.example.PartTrip.tripcard.dto.response;

import com.example.PartTrip.tripcard.entity.TripCardEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class TripCardDetailResponse {

    private Long cardId;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<TimelineItemResponse> timeline;

    public static TripCardDetailResponse from(TripCardEntity tripCard, List<TimelineItemResponse> timeline) {
        return TripCardDetailResponse.builder()
                .cardId(tripCard.getTripCardId())
                .startDate(tripCard.getStartDate())
                .endDate(tripCard.getEndDate())
                .timeline(timeline)
                .build();
    }


}
