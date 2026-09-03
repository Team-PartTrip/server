package com.example.PartTrip.tripcard.repository;

import com.example.PartTrip.tripcard.entity.TripCardPhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface TripCardPhotoRepository extends JpaRepository<TripCardPhotoEntity, Long> {

    List<TripCardPhotoEntity> findByTripCardIdOrderByTakenAtAsc(Long tripCardId);

    // 카드를 지울 때 남은 이미지 파일을 정리하려고 URL 을 먼저 모은다
    List<TripCardPhotoEntity> findByTripCardIdIn(Collection<Long> tripCardIds);

    List<TripCardPhotoEntity> findByTripCardPlaceIdOrderBySortOrderAsc(Long tripCardPlaceId);

    long countByTripCardId(Long tripCardId);

    void deleteByTripCardId(Long tripCardId);

    // Func-007-01 프로필 통계의 "기록" 수.
    // trip_card_photo 에는 user_id 가 없어서 내 여행 카드를 거쳐 센다.
    @Query("""
            select count(p) from TripCardPhotoEntity p
            where p.tripCardId in (
                select c.tripCardId from TripCardEntity c where c.userId = :userId
            )
            """)
    long countByUserId(@Param("userId") String userId);
}
