package com.example.PartTrip.domain.community.repository;

import com.example.PartTrip.domain.community.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    // 특정 여행지의 리뷰를 최신순으로 조회
    Page<ReviewEntity> findByCountryInfoIdOrderByCreateDateDesc(Long countryInfoId, Pageable pageable);

    // 전체 리뷰를 최신순으로 조회 (커뮤니티 피드용)
    Page<ReviewEntity> findAllByOrderByCreateDateDesc(Pageable pageable);

    // 내가 작성한 리뷰 목록
    Page<ReviewEntity> findByUserIdOrderByCreateDateDesc(String userId, Pageable pageable);
}
