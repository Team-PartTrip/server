package com.example.PartTrip.tripcard.service;

import com.example.PartTrip.notification.event.RegionVisitedEvent;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripStartNotifier {

    private final TripCardRepository tripCardRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public int announceTripsStartingOn(LocalDate date) {
        List<TripCardEntity> cards = tripCardRepository.findByStartDateAndRegionCodeIsNotNull(date);
        cards.forEach(card -> eventPublisher.publishEvent(
                new RegionVisitedEvent(card.getRegionCode(), card.getUserId())));
        return cards.size();
    }
}
