package com.example.PartTrip.region.service;

import com.example.PartTrip.region.dto.response.RegionMapResponseDto;
import com.example.PartTrip.region.enums.RegionCode;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 대한민국 지도 — 내가 다녀온 시·도 (#162)
 *
 * 방문 지역 테이블을 두지 않는다. 여행카드에 시·도가 들어 있으니 카드를 묶어
 * 세면 그대로 방문 기록이다. 따로 쌓아 두면 카드를 지울 때 같이 줄여 줘야 하고,
 * 그 처리를 한 군데라도 빠뜨리면 지도에 다녀오지도 않은 곳이 남는다.
 */
@Service
@RequiredArgsConstructor
public class RegionMapService {

    private final TripCardRepository tripCardRepository;

    @Transactional(readOnly = true)
    public RegionMapResponseDto getRegionMap(String userId) {
        List<RegionMapResponseDto.VisitedRegionResponseDto> visited =
                tripCardRepository.countTripsByRegion(userId).stream()
                        // 지도가 쓰지 않는 코드가 섞여 있어도 지도 전체를 못 그리게 두지 않는다
                        .filter(row -> RegionCode.exists(row.getRegionCode()))
                        .map(row -> RegionMapResponseDto.VisitedRegionResponseDto.builder()
                                .regionCode(row.getRegionCode())
                                .regionName(RegionCode.nameOf(row.getRegionCode()))
                                .tripCount(row.getTripCount())
                                .build())
                        .sorted(Comparator.comparing(
                                RegionMapResponseDto.VisitedRegionResponseDto::getRegionCode))
                        .toList();

        return RegionMapResponseDto.builder()
                .totalRegions(RegionCode.values().length)
                .visited(visited)
                .trips(trips(userId))
                .build();
    }

    private List<RegionMapResponseDto.TripResponseDto> trips(String userId) {
        List<TripCardEntity> cards = tripCardRepository.findByUserIdAndRegionCodeIsNotNull(userId).stream()
                .filter(c -> RegionCode.exists(c.getRegionCode()))
                .toList();
        if (cards.isEmpty()) {
            return List.of();
        }
        List<Long> ids = cards.stream().map(TripCardEntity::getTripCardId).toList();

        // 소수 넷째 자리(약 10m)에서 같은 곳이면 한 점으로 본다
        Map<Long, Set<List<Double>>> points = new LinkedHashMap<>();
        Stream.concat(tripCardRepository.findPlacePoints(ids).stream(),
                        tripCardRepository.findPhotoPoints(ids).stream())
                .forEach(p -> points.computeIfAbsent(p.getTripCardId(), k -> new LinkedHashSet<>())
                        .add(List.of(round(p.getLatitude()), round(p.getLongitude()))));

        return cards.stream()
                .map(c -> RegionMapResponseDto.TripResponseDto.builder()
                        .tripCardId(c.getTripCardId())
                        .regionCode(c.getRegionCode())
                        .cityName(c.getCityName())
                        .points(points.getOrDefault(c.getTripCardId(), Set.of()).stream()
                                .map(xy -> new double[]{xy.get(0), xy.get(1)})
                                .toList())
                        .build())
                .toList();
    }

    private static double round(double v) {
        return Math.round(v * 10_000) / 10_000.0;
    }
}
