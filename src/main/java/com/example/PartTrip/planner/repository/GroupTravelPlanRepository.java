package com.example.PartTrip.planner.repository;

import com.example.PartTrip.main.dto.PopularCityResponseDto;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GroupTravelPlanRepository extends JpaRepository<GroupTravelPlanEntity, Long> {

    List<GroupTravelPlanEntity> findByGroupIdOrderByStartDateDesc(Long groupId);

    Optional<GroupTravelPlanEntity> findByPlanIdAndGroupId(Long planId, Long groupId);

    // 플래너 상세 화면에 보여줄 가장 최근 여행 계획
    Optional<GroupTravelPlanEntity> findFirstByGroupIdOrderByCreatedAtDesc(Long groupId);

    // 여러 그룹의 최신 여행 계획을 목록 조회용으로 한 번에 가져온다
    List<GroupTravelPlanEntity> findByGroupIdInOrderByCreatedAtDesc(List<Long> groupIds);

    /**
     * 인기 여행지 (API-008-11).
     *
     * 여행 계획이 많이 만들어진 도시 순이다. 별도 집계 테이블을 두지 않고
     * 계획 표를 그대로 센다. 계획 수가 적은 동안은 이 편이 정확하다.
     *
     * 나라·도시가 비어 있는 계획이 있다. 그룹 만들기 다음 화면에서 채우므로
     * 아직 목적지를 안 고른 계획이다. 세지 않는다.
     *
     * 같은 도시 이름이 다른 나라에 있다(산호세 — 코스타리카·미국).
     * 계획 수까지 같으면 순서가 정해지지 않아 limit 경계에서 목록이
     * 호출마다 바뀐다. 나라 이름까지 정렬 기준에 넣는다.
     */
    @Query("""
            select new com.example.PartTrip.main.dto.PopularCityResponseDto(
                       p.cityName, p.countryName, count(p))
              from GroupTravelPlanEntity p
             where p.cityName is not null and p.cityName <> ''
               and p.countryName is not null and p.countryName <> ''
             group by p.cityName, p.countryName
             order by count(p) desc, p.cityName asc, p.countryName asc
            """)
    List<PopularCityResponseDto> findPopularCities(Pageable pageable);
}
