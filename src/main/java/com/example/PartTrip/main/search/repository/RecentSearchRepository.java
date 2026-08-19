package com.example.PartTrip.main.search.repository;

import com.example.PartTrip.main.search.entity.RecentSearchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecentSearchRepository
        extends JpaRepository<RecentSearchEntity, Long> {

    List<RecentSearchEntity> findByUserIdOrderBySearchedAtDesc(String userId);

    void deleteByUserId(String userId);

}