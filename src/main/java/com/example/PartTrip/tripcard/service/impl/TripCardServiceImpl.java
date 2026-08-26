package com.example.PartTrip.tripcard.service.impl;

import com.example.PartTrip.photo.service.CurrentUserProvider;
import com.example.PartTrip.tripcard.dto.response.TripCardDetailResponse;
import com.example.PartTrip.tripcard.dto.response.TripCardResponse;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import com.example.PartTrip.tripcard.service.TripCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TripCardServiceImpl implements TripCardService {

    private final TripCardRepository tripCardRepository;
    private final CurrentUserProvider currentUserProvider;


    @Transactional(readOnly = true)
    @Override
    public TripCardDetailResponse getTripCard(Long tripCardId) {
        String currentUserId = currentUserProvider.getCurrentUserId();

        TripCardEntity tripCard = tripCardRepository.findByTripCardId(tripCardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카드가 존재하지 않습니다."));

        if (!tripCard.getUserId().equals( currentUserId)) {
            throw new IllegalStateException("이 카드를 조회할 권한이 없습니다.");
        }

        return TripCardDetailResponse.from(tripCard, );
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

    @Transactional
    @Override
    public String deleteTripCard(Set<Long> tripCardIds) {

        String currentUserId = currentUserProvider.getCurrentUserId();

        List<TripCardEntity> tripCards = tripCardRepository.findAllById(tripCardIds);

        if (tripCards.size() != tripCardIds.size()) {
            throw new IllegalArgumentException("조회 중 일부 카드가 누락됨을 감지했습니다.");
        }

        for (TripCardEntity tripCard : tripCards) {
            if (!tripCard.getUserId().equals(currentUserId)) {
                throw new IllegalStateException("이 카드를 삭제할 권한이 없습니다.");
            }
        }

        tripCardRepository.deleteAllById(tripCardIds);

        return "삭제 완료";
    }

}
