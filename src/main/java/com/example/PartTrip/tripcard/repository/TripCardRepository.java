package com.example.PartTrip.tripcard.repository;

import com.example.PartTrip.tripcard.entity.TripCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.util.Collection;

public interface TripCardRepository extends JpaRepository<TripCardEntity, Long> {

    // Func-003-02 "여행카드들을 시간순으로 조회"
    List<TripCardEntity> findByUserIdOrderByStartDateDesc(String userId);

    // 대한민국 지도 (#162). 방문 지역은 따로 저장하지 않고 카드를 시·도로 묶어 센다.
    // 카드가 곧 방문 기록이라, 따로 두면 둘이 어긋날 자리만 생긴다.
    @Query("""
            SELECT c.regionCode AS regionCode, COUNT(c) AS tripCount
            FROM TripCardEntity c
            WHERE c.userId = :userId AND c.regionCode IS NOT NULL
            GROUP BY c.regionCode
            """)
    List<RegionTripCount> countTripsByRegion(String userId);

    interface RegionTripCount {
        String getRegionCode();
        long getTripCount();
    }

    List<TripCardEntity> findByUserIdAndRegionCodeIsNotNull(String userId);

    @Query("""
            SELECT p.tripCardId AS tripCardId, p.latitude AS latitude, p.longitude AS longitude
            FROM TripCardPlaceEntity p
            WHERE p.tripCardId IN :ids AND p.latitude IS NOT NULL AND p.longitude IS NOT NULL
            """)
    List<CardPoint> findPlacePoints(Collection<Long> ids);

    @Query("""
            SELECT p.tripCardId AS tripCardId, p.latitude AS latitude, p.longitude AS longitude
            FROM TripCardPhotoEntity p
            WHERE p.tripCardId IN :ids AND p.latitude IS NOT NULL AND p.longitude IS NOT NULL
            """)
    List<CardPoint> findPhotoPoints(Collection<Long> ids);

    interface CardPoint {
        Long getTripCardId();
        Double getLatitude();
        Double getLongitude();
    }

    @Query("""
            SELECT COUNT(DISTINCT c.regionCode) FROM TripCardEntity c
            WHERE c.userId = :userId AND c.regionCode IS NOT NULL
            """)
    long countDistinctRegionsByUserId(String userId);

    // 조회 · 수정 · 삭제 시 소유자까지 함께 확인한다
    Optional<TripCardEntity> findByTripCardIdAndUserId(Long tripCardId, String userId);

    Optional<TripCardEntity> findByPlanIdAndUserId(Long planId, String userId);

    // 플래너를 지울 때 그 플래너로 만들어진 카드를 찾는다.
    // 카드는 지우지 않고 planId 만 끊는다.
    List<TripCardEntity> findByPlanIdIn(Collection<Long> planIds);

    List<TripCardEntity> findByPlanIdAndUserIdIn(Long planId, Collection<String> userIds);

    // Func-007-01 프로필 통계의 "여행" 수
    // 종료일이 지났는데 아직 잠기지 않은 카드 (TripCardScheduler)
    List<TripCardEntity> findByDateOverFalseAndEndDateBefore(LocalDate date);

    long countByUserId(String userId);
}
