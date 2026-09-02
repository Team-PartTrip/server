package com.example.PartTrip.main.repository;

import com.example.PartTrip.main.dto.PopularCityResponseDto;
import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.enums.TourPlaceCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TourPlaceRepository extends JpaRepository<TourPlaceEntity, Long> {

    // 나라별 관광지 조회
    List<TourPlaceEntity> findByCountryName(String countryName);

    // 나라 + 도시 + 카테고리로 조회 (도시·카테고리는 null 이면 조건에서 제외)
    @Query("""
            SELECT p FROM TourPlaceEntity p
            WHERE p.countryName = :countryName
              AND (:cityName IS NULL OR p.cityName = :cityName)
              AND (:category IS NULL OR p.category = :category)
            ORDER BY p.rating DESC NULLS LAST, p.placeName ASC
            """)
    List<TourPlaceEntity> search(String countryName,
                                 String cityName,
                                 TourPlaceCategory category);

    /**
     * 인기 여행지 (API-005-11).
     *
     * 고를 수 있는 도시는 "장소가 있는 도시" 다. 그래서 tour_place 에서 뽑는다.
     * 계획 표에서 뽑으면 장소가 없는 도시(도쿄 · 리브르빌)가 목록에 올라오고,
     * 고르면 바로 다음 화면인 장소 담기가 빈 채로 뜬다.
     *
     * 순서는 그 도시로 만들어진 여행 계획 수다. 계획이 하나도 없어도
     * 도시는 목록에 남는다. 그래야 첫 사용자에게도 고를 것이 보인다.
     *
     * 같은 도시 이름이 다른 나라에 있다(산호세 — 코스타리카·미국).
     * 계획 수까지 같으면 순서가 정해지지 않아 limit 경계에서 목록이
     * 호출마다 바뀐다. 나라 이름까지 정렬 기준에 넣는다.
     */
    @Query("""
            select new com.example.PartTrip.main.dto.PopularCityResponseDto(
                       t.cityName, t.countryName, count(distinct p.planId))
              from TourPlaceEntity t
              left join GroupTravelPlanEntity p
                     on p.cityName = t.cityName
                    and p.countryName = t.countryName
             where t.cityName is not null and t.cityName <> ''
               and t.countryName is not null and t.countryName <> ''
             group by t.cityName, t.countryName
             order by count(distinct p.planId) desc, t.cityName asc, t.countryName asc
            """)
    List<PopularCityResponseDto> findPopularCities(Pageable pageable);
}
