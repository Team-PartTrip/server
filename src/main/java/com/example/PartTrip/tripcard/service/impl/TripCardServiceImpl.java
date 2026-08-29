package com.example.PartTrip.tripcard.service.impl;

import com.example.PartTrip.global.security.CurrentUserProvider;
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
    public TripCardDetailResponse getTripCard(Long cardId) {
        String currentUserId = currentUserProvider.getCurrentUserId();

        // 내 카드가 아니면 조회 단계에서 걸러진다
        TripCardEntity tripCard = tripCardRepository
                .findByTripCardIdAndUserId(cardId, currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카드가 존재하지 않습니다."));

        // 타임라인(PLACE · PHOTO)은 아직 비어 있다 — 여행카드 담당이 채운다
        return TripCardDetailResponse.from(tripCard, List.of());
    }

    @Transactional(readOnly = true)
    @Override
    public List<TripCardResponse> getTripCards() {
        String currentUserId = currentUserProvider.getCurrentUserId();

        // 명세서 비고: "시간순 정렬"
        return tripCardRepository.findByUserIdOrderByStartDateDesc(currentUserId)
                .stream()
                .map(TripCardResponse::from)
                .toList();
    }

    @Transactional
    @Override
    public String deleteTripCard(Set<Long> cardIds) {
        String currentUserId = currentUserProvider.getCurrentUserId();

        List<TripCardEntity> tripCards = tripCardRepository.findAllById(cardIds);

        if (tripCards.size() != cardIds.size()) {
            throw new IllegalArgumentException("조회 중 일부 카드가 누락됨을 감지했습니다.");
        }

        for (TripCardEntity tripCard : tripCards) {
            if (!tripCard.getUserId().equals(currentUserId)) {
                throw new IllegalStateException("이 카드를 삭제할 권한이 없습니다.");
            }
        }

        tripCardRepository.deleteAllById(cardIds);

        return "삭제 완료";
    }
}
