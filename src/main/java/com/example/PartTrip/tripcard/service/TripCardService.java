package com.example.PartTrip.tripcard.service;

import com.example.PartTrip.tripcard.dto.response.TripCardResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TripCardService {

    @Transactional(readOnly = true)
    TripCardResponse getTripCard(Long tripCardId);

    @Transactional(readOnly = true)
    List<TripCardResponse> getTripCards();
}
