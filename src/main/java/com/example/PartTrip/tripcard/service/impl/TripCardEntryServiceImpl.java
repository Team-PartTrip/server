package com.example.PartTrip.tripcard.service.impl;

import com.example.PartTrip.global.storage.ImageStorageService;
import com.example.PartTrip.global.security.CurrentUserProvider;
import com.example.PartTrip.tripcard.dto.request.UpdateEntryMetadataRequest;
import com.example.PartTrip.tripcard.dto.response.TripCardEntryResponse;
import com.example.PartTrip.tripcard.entity.MetadataSource;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.entity.TripCardPhotoEntity;
import com.example.PartTrip.tripcard.entity.TripCardPlaceEntity;
import com.example.PartTrip.tripcard.repository.TripCardPhotoRepository;
import com.example.PartTrip.tripcard.repository.TripCardPlaceRepository;
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

    private static final int COMMENT_MAX_LENGTH = 100;
    private static final int PLACE_NAME_MAX_LENGTH = 255;

    private final TripCardRepository tripCardRepository;
    private final TripCardPhotoRepository tripCardPhotoRepository;
    private final TripCardPlaceRepository tripCardPlaceRepository;
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
        photo.setComment(normalizeComment(comment));
        photo.setTakenAt(takenAt);
        photo.setLatitude(exif == null ? null : exif.latitude());
        photo.setLongitude(exif == null ? null : exif.longitude());
        // 여기가 EXIF 값을 넣는 유일한 경로다. 채워 넣은 자리에만 EXIF 표시를 남겨
        // 나중에 사용자가 고를 수 있는 자리(비어 있는 쪽)와 구분한다.
        if (photo.getLatitude() != null && photo.getLongitude() != null) {
            photo.setLocationSource(MetadataSource.EXIF);
        }
        if (takenAt != null) {
            photo.setTakenAtSource(MetadataSource.EXIF);
        }
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
    public TripCardEntryResponse updateComment(Long cardId, Long entryId, String comment) {
        getEditableCard(cardId);
        TripCardPhotoEntity photo = getCardPhoto(cardId, entryId);

        photo.setComment(normalizeComment(comment));

        return TripCardEntryResponse.from(photo);
    }

    // Func-003-07 촬영 위치 직접 지정
    //
    // 여행이 끝난 뒤에도 쓸 수 있어야 한다. 카카오톡으로 받은 사진은 위치가 지워져 있고,
    // 사진 정리는 여행에서 돌아온 뒤에 하는 사람이 많다. 그래서 종료 검사(getEditableCard)를
    // 하지 않고 소유권만 본다. 대신 EXIF 가 들고 있던 값은 절대 건드리지 못하게 막는다.
    // 기록을 바꾸는 게 아니라 비어 있던 자리를 채우는 것이라 기한을 두지 않는다.
    @Transactional
    @Override
    public TripCardEntryResponse updateMetadata(Long cardId, Long entryId,
                                                UpdateEntryMetadataRequest request) {
        // 요청 자체가 성립하는지 먼저 본다. 카드·사진을 읽기 전에 걸러낸다.
        boolean hasLatitude = request.getLatitude() != null;
        boolean hasLongitude = request.getLongitude() != null;
        if (hasLatitude != hasLongitude) {
            throw new IllegalArgumentException("위도와 경도는 함께 보내야 합니다.");
        }
        boolean hasLocation = hasLatitude;
        String placeName = normalizePlaceName(request.getPlaceName());
        if (placeName != null && !hasLocation) {
            throw new IllegalArgumentException("장소 이름은 좌표와 함께 보내야 합니다.");
        }
        LocalDateTime takenAt = request.getTakenAt();
        if (!hasLocation && takenAt == null) {
            throw new IllegalArgumentException("지정할 촬영 위치나 촬영 시각이 없습니다.");
        }

        getOwnedCard(cardId);
        TripCardPhotoEntity photo = getCardPhoto(cardId, entryId);

        if (hasLocation) {
            requireEditable(photo.getLocationSource(), "촬영 위치");
            photo.setLatitude(request.getLatitude());
            photo.setLongitude(request.getLongitude());
            // 이름 없이 좌표만 다시 고르면 이름도 지운다. 옮긴 좌표에 예전 장소 이름이
            // 남아 있는 쪽이 더 나쁘다.
            photo.setPlaceName(placeName);
            photo.setLocationSource(MetadataSource.MANUAL);
        }
        if (takenAt != null) {
            requireEditable(photo.getTakenAtSource(), "촬영 시각");
            // 순번은 날짜 묶음별로 매긴다. 날짜가 바뀌면 옮겨간 묶음 기준으로 다시 받아야
            // 그 날의 다른 사진과 순번이 겹치지 않는다.
            if (!sameDate(photo.getTakenAt(), takenAt.toLocalDate())) {
                photo.setSortOrder(nextSortOrder(cardId, takenAt.toLocalDate()));
            }
            photo.setTakenAt(takenAt);
            photo.setTakenAtSource(MetadataSource.MANUAL);
        }

        return TripCardEntryResponse.from(photo);
    }

    @Transactional
    @Override
    public void deleteEntry(Long cardId, Long entryId) {
        TripCardEntity tripCard = getEditableCard(cardId);
        TripCardPhotoEntity photo = getCardPhoto(cardId, entryId);

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

    @Transactional
    @Override
    public void deletePlace(Long cardId, Long placeId) {
        TripCardEntity tripCard = getEditableCard(cardId);
        TripCardPlaceEntity place = tripCardPlaceRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 장소 항목이 존재하지 않습니다."));
        if (!place.getTripCardId().equals(cardId)) {
            throw new IllegalArgumentException("해당 카드에 속한 장소 항목이 아닙니다.");
        }

        Long tourPlaceId = place.getTourPlaceId();
        tripCardPlaceRepository.delete(place);
        tripCardPlaceRepository.flush();
        boolean samePlaceRemains = tourPlaceId != null
                && tripCardPlaceRepository
                .findByTripCardIdOrderByVisitedDateAscSortOrderAsc(cardId).stream()
                .anyMatch(remaining -> tourPlaceId.equals(remaining.getTourPlaceId()));
        if (!samePlaceRemains) {
            tripCard.setPlaceCount(Math.max(
                    0, (tripCard.getPlaceCount() == null ? 0 : tripCard.getPlaceCount()) - 1));
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

    // 사진 추가와 코멘트 수정이 같은 규칙을 쓰도록 여기 한 곳에 둔다.
    // 공백만 남은 코멘트는 없는 것으로 본다. trim() 은 전각 공백(U+3000)을
    // 남기기 때문에 유니코드를 아는 strip() 을 쓴다.
    private String normalizeComment(String comment) {
        return normalizeText(comment, COMMENT_MAX_LENGTH, "코멘트는");
    }

    private String normalizePlaceName(String placeName) {
        return normalizeText(placeName, PLACE_NAME_MAX_LENGTH, "장소 이름은");
    }

    private String normalizeText(String value, int maxLength, String label) {
        if (value == null) {
            return null;
        }
        String stripped = value.strip();
        if (stripped.isEmpty()) {
            return null;
        }
        if (stripped.length() > maxLength) {
            throw new IllegalArgumentException(
                    label + " " + maxLength + "자까지 쓸 수 있습니다.");
        }
        return stripped;
    }

    /** 이 카드에 속한 사진인지까지 확인해서 가져온다 */
    private TripCardPhotoEntity getCardPhoto(Long cardId, Long entryId) {
        TripCardPhotoEntity photo = tripCardPhotoRepository.findById(entryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사진 항목이 존재하지 않습니다."));
        if (!photo.getTripCardId().equals(cardId)) {
            throw new IllegalArgumentException("해당 카드에 속한 사진 항목이 아닙니다.");
        }
        return photo;
    }

    /** 사진이 이미 들고 있던 EXIF 값은 고치지 못한다. 비어 있거나 직접 고른 값만 다시 고른다. */
    private void requireEditable(MetadataSource source, String label) {
        if (source == MetadataSource.EXIF) {
            throw new IllegalArgumentException(
                    "사진에 기록된 " + label + " 정보는 바꿀 수 없습니다.");
        }
    }

    private TripCardEntity getOwnedCard(Long cardId) {
        String userId = currentUserProvider.getCurrentUserId();
        return tripCardRepository.findByTripCardIdAndUserId(cardId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카드가 없거나 수정 권한이 없습니다."));
    }

    private TripCardEntity getEditableCard(Long cardId) {
        TripCardEntity tripCard = getOwnedCard(cardId);
        if (tripCard.isDateOver()) {
            throw new IllegalStateException("여행 종료 후에는 여행 카드를 수정할 수 없습니다.");
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
