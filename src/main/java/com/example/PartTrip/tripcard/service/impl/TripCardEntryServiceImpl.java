package com.example.PartTrip.tripcard.service.impl;

import com.example.PartTrip.global.storage.ImageStorageService;
import com.example.PartTrip.global.security.CurrentUserProvider;
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
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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
        // 촬영 시각을 읽지 못하면 비워 둔다. 업로드 시각을 대신 넣으면 여행과 상관없는
        // 오늘 날짜로 타임라인에 꽂히고, 그 값이 진짜 촬영 시각인지 구분할 수 없게 된다.
        ExifMetadataUtil.ExifMetadata exif = ExifMetadataUtil.extract(imageFile).orElse(null);
        LocalDateTime takenAt = exif == null ? null : exif.takenAt();

        TripCardPhotoEntity photo = new TripCardPhotoEntity();
        photo.setTripCardId(cardId);
        String storedImageUrl = imageStorageService.store(imageFile, "trip-card/" + cardId);
        deleteFileIfRolledBack(storedImageUrl);
        photo.setImageUrl(storedImageUrl);
        photo.setComment(comment);
        photo.setTakenAt(takenAt);
        photo.setLatitude(exif == null ? null : exif.latitude());
        photo.setLongitude(exif == null ? null : exif.longitude());
        photo.setSortOrder(nextSortOrder(cardId, takenAt == null ? null : takenAt.toLocalDate()));
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
        deleteFileAfterCommit(photo.getImageUrl());
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

    // 파일과 DB 는 같은 트랜잭션에 못 묶인다. 그래서 커밋 결과를 보고 파일을 맞춘다.
    // 저장에 실패하면 올려둔 파일을 지우고, 삭제가 확정되면 그때 파일을 지운다.
    private void deleteFileIfRolledBack(String imageUrl) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status != STATUS_COMMITTED) {
                    imageStorageService.delete(imageUrl);
                }
            }
        });
    }

    private void deleteFileAfterCommit(String imageUrl) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            imageStorageService.delete(imageUrl);
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                imageStorageService.delete(imageUrl);
            }
        });
    }

    private TripCardEntity getEditableCard(Long cardId) {
        String userId = currentUserProvider.getCurrentUserId();
        TripCardEntity tripCard = tripCardRepository.findByTripCardIdAndUserId(cardId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카드가 없거나 수정 권한이 없습니다."));
        if (tripCard.isDateOver()) {
            throw new IllegalStateException("여행 종료 후에는 사진을 추가하거나 삭제할 수 없습니다.");
        }
        return tripCard;
    }

    // 같은 날짜끼리 순번을 매긴다. 날짜를 모르는 사진끼리도 한 묶음이다.
    private int nextSortOrder(Long cardId, LocalDate date) {
        return tripCardPhotoRepository.findByTripCardIdOrderByTakenAtAsc(cardId).stream()
                .filter(photo -> sameDate(photo.getTakenAt(), date))
                .map(TripCardPhotoEntity::getSortOrder)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }

    static boolean sameDate(LocalDateTime takenAt, LocalDate date) {
        return Objects.equals(takenAt == null ? null : takenAt.toLocalDate(), date);
    }
}
