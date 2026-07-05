package com.example.PartTrip.service.community;

import com.example.PartTrip.dto.community.TripPlaceRequestDto;
import com.example.PartTrip.dto.community.TripPlaceResponseDto;
import com.example.PartTrip.dto.community.TripRequestDto;
import com.example.PartTrip.dto.community.TripResponseDto;
import com.example.PartTrip.entity.community.TripEntity;
import com.example.PartTrip.entity.community.TripPlaceEntity;
import com.example.PartTrip.entity.main.CountryInfoEntity;
import com.example.PartTrip.entity.signup.UserEntity;
import com.example.PartTrip.repository.community.TripPlaceRepository;
import com.example.PartTrip.repository.community.TripRepository;
import com.example.PartTrip.repository.main.CountryInfoRepository;
import com.example.PartTrip.repository.signup.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final TripPlaceRepository tripPlaceRepository;
    private final CountryInfoRepository countryInfoRepository;
    private final UserRepository userRepository;

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

        return toDto(savedTrip);
    }

    // 내가 만든 일정 목록
    public List<TripResponseDto> getMyTrips(String userId) {
        return tripRepository.findByUserIdOrderByCreateDateDesc(userId)
                .stream()
                .map(this::toDto)
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

        return toDto(trip);
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

        return toDto(saved);
    }

    // 공유된 일정 목록 (커뮤니티 피드)
    public List<TripResponseDto> listSharedTrips() {
        return tripRepository.findByIsPublicTrueOrderByCreateDateDesc()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 공유된 일정 상세 (공개된 일정만 조회 가능)
    public TripResponseDto getSharedTripDetail(Long tripId) {
        TripEntity trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다."));

        if (!Boolean.TRUE.equals(trip.getIsPublic())) {
            throw new IllegalArgumentException("공개되지 않은 일정입니다.");
        }

        return toDto(trip);
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

        return toDto(savedCopy);
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

    // Entity -> Dto 변환 (장소 목록 + 국가정보 + 닉네임 포함)
    private TripResponseDto toDto(TripEntity trip) {
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
                trip.getIsPublic(),
                trip.getCreateDate(),
                places
        );
    }
}
