package com.example.PartTrip.tripcard.service.impl;

import com.example.PartTrip.photo.service.CurrentUserProvider;
import com.example.PartTrip.tripcard.dto.response.TripCardResponse;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import com.example.PartTrip.tripcard.service.TripCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripCardServiceImpl implements TripCardService {

    private final TripCardRepository tripCardRepository;
    private final CurrentUserProvider currentUserProvider;


    @Transactional(readOnly = true)
    @Override
    public TripCardResponse getTripCard(Long tripCardId) {
        String currentUserId = currentUserProvider.getCurrentUserId();

        TripCardEntity tripCard = tripCardRepository.findByTripCardId(tripCardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카드가 존재하지 않습니다."));

        if (!tripCard.getUserId().equals( currentUserId)) {
            throw new IllegalStateException("이 카드를 조회할 권한이 없습니다.");
        }

        return TripCardResponse.from(tripCard);
    }


    @Transactional(readOnly = true)
    @Override
    public List<TripCardResponse> getTripCards() {
        String currentUserId = currentUserProvider.getCurrentUserId();

        List<TripCardEntity> tripCards = tripCardRepository.findTripCardEntitiesByUserId(currentUserId);

        List<TripCardResponse> tripCardResponsesList = tripCards.stream()
                .map(TripCardResponse::from)
                .toList();

        return tripCardResponsesList;
    }

}
