package com.example.PartTrip.tripcard.service;

import com.example.PartTrip.tripcard.dto.response.TripCardDetailResponse;
import com.example.PartTrip.tripcard.dto.response.TripCardResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface TripCardService {

    @Transactional(readOnly = true)
    TripCardDetailResponse getTripCard(Long tripCardId);

    @Transactional(readOnly = true)
    List<TripCardResponse> getTripCards();

    @Transactional
    String deleteTripCard(Set<Long> tripCardIds);
}
