package com.example.PartTrip.main.repository;

import com.example.PartTrip.main.entity.FestivalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface FestivalRepository extends JpaRepository<FestivalEntity, Long> {

    List<FestivalEntity> findByCountryName(String countryName);

    @Query("""
            SELECT f FROM FestivalEntity f
            WHERE f.countryName = :countryName
              AND f.startDate <= :monthEnd
              AND COALESCE(f.endDate, f.startDate) >= :monthStart
            ORDER BY f.startDate
            """)
    List<FestivalEntity> findInMonth(
            @Param("countryName") String countryName,
            @Param("monthStart") String monthStart,
            @Param("monthEnd") String monthEnd);

    List<FestivalEntity> findBySourceIdIn(Collection<String> sourceIds);
}
