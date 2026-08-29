package com.example.PartTrip.tripcard.service.impl;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.photo.service.CurrentUserProvider;
import com.example.PartTrip.tripcard.dto.response.TripCardDetailResponse;
import com.example.PartTrip.tripcard.dto.response.TripCardResponse;
import com.example.PartTrip.tripcard.dto.response.TimelineItemResponse;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.entity.TripCardPhotoEntity;
import com.example.PartTrip.tripcard.entity.TripCardPlaceEntity;
import com.example.PartTrip.tripcard.entity.TimelineItemType;
import com.example.PartTrip.tripcard.repository.TripCardPhotoRepository;
import com.example.PartTrip.tripcard.repository.TripCardPlaceRepository;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import com.example.PartTrip.tripcard.service.TripCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripCardServiceImpl implements TripCardService {

    private final TripCardRepository tripCardRepository;
    private final CurrentUserProvider currentUserProvider;
    private final TripCardPlaceRepository tripCardPlaceRepository;
    private final TripCardPhotoRepository tripCardPhotoRepository;
    private final TourPlaceRepository tourPlaceRepository;


    @Transactional(readOnly = true)
    @Override
    public TripCardDetailResponse getTripCard(Long tripCardId) {
        String currentUserId = currentUserProvider.getCurrentUserId();

        TripCardEntity tripCard = tripCardRepository.findByTripCardId(tripCardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카드가 존재하지 않습니다."));

        if (!tripCard.getUserId().equals( currentUserId)) {
            throw new IllegalStateException("이 카드를 조회할 권한이 없습니다.");
        }

        List<TripCardPlaceEntity> places = tripCardPlaceRepository
                .findByTripCardIdOrderByVisitedDateAscSortOrderAsc(tripCardId);
        List<TripCardPhotoEntity> photos = tripCardPhotoRepository
                .findByTripCardIdOrderByTakenAtAsc(tripCardId);
        return TripCardDetailResponse.from(tripCard, buildTimeline(places, photos));
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

    private List<TimelineItemResponse> buildTimeline(List<TripCardPlaceEntity> places,
                                                     List<TripCardPhotoEntity> photos) {
        Map<Long, TourPlaceEntity> tourPlacesById = tourPlaceRepository.findAllById(places.stream()
                        .map(TripCardPlaceEntity::getTourPlaceId)
                        .filter(java.util.Objects::nonNull)
                        .toList()).stream()
                .collect(Collectors.toMap(TourPlaceEntity::getTourPlaceId, Function.identity()));

        List<TimelineEntry> entries = new ArrayList<>();
        for (TripCardPlaceEntity place : places) {
            TourPlaceEntity tourPlace = place.getTourPlaceId() == null ? null
                    : tourPlacesById.get(place.getTourPlaceId());
            entries.add(new TimelineEntry(place.getVisitedDate(), 0, null, place.getSortOrder(),
                    TimelineItemResponse.builder()
                            .date(place.getVisitedDate()).type(TimelineItemType.PLACE)
                            .placeName(place.getPlaceName()).address(place.getAddress())
                            .rating(tourPlace == null ? null : tourPlace.getRating())
                            .latitude(place.getLatitude()).longitude(place.getLongitude()).build()));
        }
        for (TripCardPhotoEntity photo : photos) {
            LocalDate date = photo.getTakenAt() == null ? null : photo.getTakenAt().toLocalDate();
            boolean hasLocation = photo.getLatitude() != null && photo.getLongitude() != null;
            entries.add(new TimelineEntry(date, 1, photo.getTakenAt(), photo.getSortOrder(),
                    TimelineItemResponse.builder()
                            .date(date).type(hasLocation ? TimelineItemType.PHOTO : TimelineItemType.NO_INFO_PHOTO)
                            .imageUrl(photo.getImageUrl()).comment(photo.getComment()).takenAt(photo.getTakenAt())
                            .latitude(photo.getLatitude()).longitude(photo.getLongitude()).build()));
        }
        return entries.stream()
                .sorted(Comparator.comparing(TimelineEntry::date, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparingInt(TimelineEntry::typeOrder)
                        .thenComparing(TimelineEntry::takenAt,
                                Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(TimelineEntry::sortOrder, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(TimelineEntry::response)
                .toList();
    }

    private record TimelineEntry(LocalDate date, int typeOrder, LocalDateTime takenAt, Integer sortOrder,
                                 TimelineItemResponse response) { }
}
