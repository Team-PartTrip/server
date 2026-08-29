package com.example.PartTrip.tripcard.service.impl;

import com.example.PartTrip.global.storage.ImageStorageService;
import com.example.PartTrip.photo.service.CurrentUserProvider;
import com.example.PartTrip.tripcard.dto.response.TripCardEntryResponse;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.entity.TripCardPhotoEntity;
import com.example.PartTrip.tripcard.repository.TripCardPhotoRepository;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import com.example.PartTrip.tripcard.service.TripCardEntryService;
import com.example.PartTrip.tripcard.util.ExifMetadataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripCardEntryServiceImpl implements TripCardEntryService {

    private final TripCardRepository tripCardRepository;
    private final TripCardPhotoRepository tripCardPhotoRepository;
    private final CurrentUserProvider currentUserProvider;
    private final ImageStorageService imageStorageService;

    @Transactional
    @Override
    public TripCardEntryResponse addEntry(Long cardId, MultipartFile imageFile, String comment) {
        TripCardEntity tripCard = getEditableCard(cardId);
        ExifMetadataUtil.ExifMetadata exif = ExifMetadataUtil.extract(imageFile).orElse(null);
        LocalDateTime takenAt = exif == null ? LocalDateTime.now() : exif.takenAt();

        TripCardPhotoEntity photo = new TripCardPhotoEntity();
        photo.setTripCardId(cardId);
        photo.setImageUrl(imageStorageService.store(imageFile, "trip-card/" + cardId));
        photo.setComment(comment);
        photo.setTakenAt(takenAt);
        photo.setLatitude(exif == null ? null : exif.latitude());
        photo.setLongitude(exif == null ? null : exif.longitude());
        photo.setSortOrder(nextSortOrder(cardId, takenAt.toLocalDate()));
        TripCardPhotoEntity savedPhoto = tripCardPhotoRepository.save(photo);

        tripCard.setPhotoCount((tripCard.getPhotoCount() == null ? 0 : tripCard.getPhotoCount()) + 1);
        if (tripCard.getCoverImageUrl() == null) {
            tripCard.setCoverImageUrl(savedPhoto.getImageUrl());
        }
        return TripCardEntryResponse.from(savedPhoto);
    }

    @Transactional
    @Override
    public void deleteEntry(Long cardId, Long entryId) {
        TripCardEntity tripCard = getEditableCard(cardId);
        TripCardPhotoEntity photo = tripCardPhotoRepository.findById(entryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사진 항목이 존재하지 않습니다."));
        if (!photo.getTripCardId().equals(cardId)) {
            throw new IllegalArgumentException("해당 카드에 속한 사진 항목이 아닙니다.");
        }

        boolean wasCoverImage = photo.getImageUrl().equals(tripCard.getCoverImageUrl());
        tripCardPhotoRepository.delete(photo);
        tripCard.setPhotoCount(Math.max(0, (tripCard.getPhotoCount() == null ? 0 : tripCard.getPhotoCount()) - 1));
        if (wasCoverImage) {
            tripCardPhotoRepository.flush();
            tripCard.setCoverImageUrl(tripCardPhotoRepository.findByTripCardIdOrderByTakenAtAsc(cardId).stream()
                    .min(Comparator.comparing(TripCardPhotoEntity::getTakenAt,
                            Comparator.nullsLast(Comparator.naturalOrder()))
                            .thenComparing(TripCardPhotoEntity::getSortOrder,
                                    Comparator.nullsLast(Comparator.naturalOrder())))
                    .map(TripCardPhotoEntity::getImageUrl)
                    .orElse(null));
        }
    }

    private TripCardEntity getEditableCard(Long cardId) {
        String userId = currentUserProvider.getCurrentUserId();
        TripCardEntity tripCard = tripCardRepository.findByTripCardIdAndUserId(cardId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카드가 없거나 수정 권한이 없습니다."));
        if (tripCard.getIsDateOver()) {
            throw new IllegalStateException("여행 종료 후에는 사진을 추가하거나 삭제할 수 없습니다.");
        }
        return tripCard;
    }

    private int nextSortOrder(Long cardId, LocalDate date) {
        List<TripCardPhotoEntity> photos = tripCardPhotoRepository.findByTripCardIdOrderByTakenAtAsc(cardId);
        return photos.stream()
                .filter(photo -> photo.getTakenAt() != null && date.equals(photo.getTakenAt().toLocalDate()))
                .map(TripCardPhotoEntity::getSortOrder)
                .filter(java.util.Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }
}
