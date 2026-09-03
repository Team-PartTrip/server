package com.example.PartTrip.tripcard.dto.response;

import com.example.PartTrip.tripcard.entity.TripCardEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class TripCardResponse {
    private Long cardId;
    private String countryName;
    private String cityName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String coverImageUrl;
    private Integer photoCount;


    public static TripCardResponse from(TripCardEntity tripCard) {
        return TripCardResponse.builder()
                .cardId(tripCard.getTripCardId())
                .countryName(tripCard.getCountryName())
                .cityName(tripCard.getCityName())
                .startDate(tripCard.getStartDate())
                .endDate(tripCard.getEndDate())
                .coverImageUrl(tripCard.getCoverImageUrl())
                .photoCount(tripCard.getPhotoCount())
                .build();
    }

}
