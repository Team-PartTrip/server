package com.example.PartTrip.service.community;

import com.example.PartTrip.dto.community.PageResponseDto;
import com.example.PartTrip.dto.community.TripPlaceRequestDto;
import com.example.PartTrip.dto.community.TripPlaceResponseDto;
import com.example.PartTrip.dto.community.TripRequestDto;
import com.example.PartTrip.dto.community.TripResponseDto;
import com.example.PartTrip.entity.community.PostImageEntity;
import com.example.PartTrip.entity.community.TripEntity;
import com.example.PartTrip.entity.community.TripPlaceEntity;
import com.example.PartTrip.entity.main.CountryInfoEntity;
import com.example.PartTrip.entity.signup.UserEntity;
import com.example.PartTrip.repository.community.CommentRepository;
import com.example.PartTrip.repository.community.LikeRepository;
import com.example.PartTrip.repository.community.PostImageRepository;
import com.example.PartTrip.repository.community.TripPlaceRepository;
import com.example.PartTrip.repository.community.TripRepository;
import com.example.PartTrip.repository.main.CountryInfoRepository;
import com.example.PartTrip.repository.signup.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TripService {

    private static final String TARGET_TYPE = "TRIP";

    private final TripRepository tripRepository;
    private final TripPlaceRepository tripPlaceRepository;
    private final CountryInfoRepository countryInfoRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final PostImageRepository postImageRepository;
    private final CommentRepository commentRepository;

    // 일정 생성
    public TripResponseDto createTrip(String userId, TripRequestDto dto) {

        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new IllegalArgumentException("일정 제목을 입력해주세요.");
        }

        CountryInfoEntity countryInfo = countryInfoRepository.findById(dto.getCountryInfoId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 여행지입니다."));

        userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        TripEntity trip = new TripEntity();
        trip.setUserId(userId);
        trip.setTitle(dto.getTitle());
        trip.setCountryInfoId(countryInfo.getCountryInfoId());
        trip.setStartDate(dto.getStartDate());
        trip.setEndDate(dto.getEndDate());
        trip.setContent(dto.getContent());
        trip.setIsPublic(false);
        trip.setCreateDate(LocalDateTime.now());

        TripEntity savedTrip = tripRepository.save(trip);

        savePlaces(savedTrip.getTripId(), dto.getPlaces());
        saveImages(savedTrip.getTripId(), dto.getImages());

        return toDto(savedTrip, userId);
    }

    // 내가 만든 일정 목록
    public List<TripResponseDto> getMyTrips(String userId) {
        return tripRepository.findByUserIdOrderByCreateDateDesc(userId)
                .stream()
                .map(t -> toDto(t, userId))
                .collect(Collectors.toList());
    }

    // 일정 상세 (본인 소유이거나 공개된 일정만 조회 가능)
    public TripResponseDto getTrip(String userId, Long tripId) {
        TripEntity trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다."));

        boolean isOwner = trip.getUserId().equals(userId);
        if (!isOwner && !Boolean.TRUE.equals(trip.getIsPublic())) {
            throw new IllegalArgumentException("공개되지 않은 일정입니다.");
        }

        return toDto(trip, userId);
    }

    // 일정 수정 (본인 소유만 가능, 장소/이미지 전체 교체)
    public TripResponseDto updateTrip(String userId, Long tripId, TripRequestDto dto) {
        TripEntity trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다."));

        if (!trip.getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인의 일정만 수정할 수 있습니다.");
        }

        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new IllegalArgumentException("일정 제목을 입력해주세요.");
        }

        CountryInfoEntity countryInfo = countryInfoRepository.findById(dto.getCountryInfoId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 여행지입니다."));

        trip.setTitle(dto.getTitle());
        trip.setCountryInfoId(countryInfo.getCountryInfoId());
        trip.setStartDate(dto.getStartDate());
        trip.setEndDate(dto.getEndDate());
        trip.setContent(dto.getContent());

        TripEntity saved = tripRepository.save(trip);

        tripPlaceRepository.deleteByTripId(tripId);
        savePlaces(tripId, dto.getPlaces());

        postImageRepository.deleteByTargetTypeAndTargetId(TARGET_TYPE, tripId);
        saveImages(tripId, dto.getImages());

        return toDto(saved, userId);
    }

    // 일정 삭제 (본인 소유만 가능)
    public void deleteTrip(String userId, Long tripId) {
        TripEntity trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다."));

        if (!trip.getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인의 일정만 삭제할 수 있습니다.");
        }

        commentRepository.deleteByTargetTypeAndTargetId(TARGET_TYPE, tripId);
        likeRepository.deleteByTargetTypeAndTargetId(TARGET_TYPE, tripId);
        postImageRepository.deleteByTargetTypeAndTargetId(TARGET_TYPE, tripId);
        tripPlaceRepository.deleteByTripId(tripId);
        tripRepository.delete(trip);
    }

    // 일정을 커뮤니티에 공개 공유
    public TripResponseDto shareTrip(String userId, Long tripId) {
        TripEntity trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다."));

        if (!trip.getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인의 일정만 공유할 수 있습니다.");
        }

        trip.setIsPublic(true);
        TripEntity saved = tripRepository.save(trip);

        return toDto(saved, userId);
    }

    // 공유된 일정 목록 (커뮤니티 피드, 페이지네이션)
    public PageResponseDto<TripResponseDto> listSharedTrips(String currentUserId, int page, int size) {
        Page<TripEntity> result =
                tripRepository.findByIsPublicTrueOrderByCreateDateDesc(PageRequest.of(page, size));

        List<TripResponseDto> content = result.getContent().stream()
                .map(t -> toDto(t, currentUserId))
                .collect(Collectors.toList());

        return new PageResponseDto<>(
                content,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.hasNext()
        );
    }

    // 공유된 일정 상세 (공개된 일정만 조회 가능)
    public TripResponseDto getSharedTripDetail(Long tripId, String currentUserId) {
        TripEntity trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다."));

        if (!Boolean.TRUE.equals(trip.getIsPublic())) {
            throw new IllegalArgumentException("공개되지 않은 일정입니다.");
        }

        return toDto(trip, currentUserId);
    }

    // 다른 사람의 공유 일정을 내 일정으로 가져오기(복사)
    public TripResponseDto importTrip(String userId, Long tripId) {
        TripEntity source = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다."));

        if (!Boolean.TRUE.equals(source.getIsPublic())) {
            throw new IllegalArgumentException("공개되지 않은 일정입니다.");
        }

        userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        TripEntity copy = new TripEntity();
        copy.setUserId(userId);
        copy.setTitle(source.getTitle());
        copy.setCountryInfoId(source.getCountryInfoId());
        copy.setStartDate(source.getStartDate());
        copy.setEndDate(source.getEndDate());
        copy.setContent(source.getContent());
        copy.setIsPublic(false);
        copy.setCreateDate(LocalDateTime.now());

        TripEntity savedCopy = tripRepository.save(copy);

        List<TripPlaceEntity> originalPlaces =
                tripPlaceRepository.findByTripIdOrderByDayNumberAscSortOrderAsc(tripId);

        for (TripPlaceEntity p : originalPlaces) {
            TripPlaceEntity newPlace = new TripPlaceEntity();
            newPlace.setTripId(savedCopy.getTripId());
            newPlace.setDayNumber(p.getDayNumber());
            newPlace.setPlaceName(p.getPlaceName());
            newPlace.setPlaceSub(p.getPlaceSub());
            newPlace.setSortOrder(p.getSortOrder());
            tripPlaceRepository.save(newPlace);
        }

        List<String> images = postImageRepository
                .findByTargetTypeAndTargetIdOrderBySortOrderAsc(TARGET_TYPE, tripId)
                .stream()
                .map(PostImageEntity::getImageUrl)
                .collect(Collectors.toList());
        saveImages(savedCopy.getTripId(), images);

        return toDto(savedCopy, userId);
    }

    // 일정 장소들 저장
    private void savePlaces(Long tripId, List<TripPlaceRequestDto> places) {
        if (places == null) return;

        int order = 0;
        for (TripPlaceRequestDto p : places) {
            if (p.getPlaceName() == null || p.getPlaceName().isBlank()) continue;

            TripPlaceEntity place = new TripPlaceEntity();
            place.setTripId(tripId);
            place.setDayNumber(p.getDayNumber() != null ? p.getDayNumber() : 1);
            place.setPlaceName(p.getPlaceName());
            place.setPlaceSub(p.getPlaceSub());
            place.setSortOrder(order++);
            tripPlaceRepository.save(place);
        }
    }

    private void saveImages(Long tripId, List<String> images) {
        if (images == null) return;
        int order = 0;
        for (String url : images) {
            if (url == null || url.isBlank()) continue;
            PostImageEntity image = new PostImageEntity();
            image.setTargetType(TARGET_TYPE);
            image.setTargetId(tripId);
            image.setImageUrl(url);
            image.setSortOrder(order++);
            postImageRepository.save(image);
        }
    }

    // Entity -> Dto 변환 (장소 목록 + 국가정보 + 닉네임 + 이미지 + 좋아요 + 댓글수 포함)
    private TripResponseDto toDto(TripEntity trip, String currentUserId) {
        CountryInfoEntity country = countryInfoRepository.findById(trip.getCountryInfoId())
                .orElse(null);

        String nickName = userRepository.findByUserId(trip.getUserId())
                .map(UserEntity::getNickName)
                .orElse("알 수 없음");

        List<TripPlaceResponseDto> places =
                tripPlaceRepository.findByTripIdOrderByDayNumberAscSortOrderAsc(trip.getTripId())
                        .stream()
                        .map(p -> new TripPlaceResponseDto(
                                p.getTripPlaceId(),
                                p.getDayNumber(),
                                p.getPlaceName(),
                                p.getPlaceSub()
                        ))
                        .collect(Collectors.toList());

        List<String> images = postImageRepository
                .findByTargetTypeAndTargetIdOrderBySortOrderAsc(TARGET_TYPE, trip.getTripId())
                .stream()
                .map(PostImageEntity::getImageUrl)
                .collect(Collectors.toList());

        long likeCount = likeRepository.countByTargetTypeAndTargetId(TARGET_TYPE, trip.getTripId());
        boolean liked = currentUserId != null && likeRepository
                .findByTargetTypeAndTargetIdAndUserId(TARGET_TYPE, trip.getTripId(), currentUserId)
                .isPresent();
        long commentCount = commentRepository.countByTargetTypeAndTargetId(TARGET_TYPE, trip.getTripId());

        return new TripResponseDto(
                trip.getTripId(),
                trip.getUserId(),
                nickName,
                trip.getTitle(),
                trip.getCountryInfoId(),
                country != null ? country.getCountryName() : null,
                country != null ? country.getCityName() : null,
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getContent(),
                images,
                likeCount,
                liked,
                commentCount,
                trip.getIsPublic(),
                trip.getCreateDate(),
                places
        );
    }
}
