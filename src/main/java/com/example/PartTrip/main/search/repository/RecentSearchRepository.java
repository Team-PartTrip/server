package com.example.PartTrip.main.search.repository;

import com.example.PartTrip.main.search.entity.RecentSearchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecentSearchRepository
        extends JpaRepository<RecentSearchEntity, Long> {

    List<RecentSearchEntity> findByUserIdOrderBySearchedAtDesc(String userId);

    void deleteByUserId(String userId);

    // 삭제 시 소유자까지 함께 확인한다 (다른 사용자의 기록을 지울 수 없도록)
    Optional<RecentSearchEntity> findByRecentSearchIdAndUserId(Long recentSearchId, String userId);

}