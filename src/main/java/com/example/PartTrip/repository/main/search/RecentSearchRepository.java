package com.example.PartTrip.repository.main.search;

import com.example.PartTrip.entity.main.search.RecentSearchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecentSearchRepository extends JpaRepository<RecentSearchEntity, Long> {

    List<RecentSearchEntity> findByUserIdOrderBySearchedAtDesc(Long userId);

    void deleteByUserId(Long userId);

}