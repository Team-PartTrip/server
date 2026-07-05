package com.example.PartTrip.repository.community;

import com.example.PartTrip.entity.community.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    // 특정 여행지의 리뷰를 최신순으로 조회
    List<ReviewEntity> findByCountryInfoIdOrderByCreateDateDesc(Long countryInfoId);

    // 전체 리뷰를 최신순으로 조회 (커뮤니티 피드용)
    List<ReviewEntity> findAllByOrderByCreateDateDesc();
}
