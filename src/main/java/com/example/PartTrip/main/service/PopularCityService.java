package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.PopularCityResponseDto;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PopularCityService {

    /** 한 번에 줄 수 있는 최대치. 화면은 넷만 쓰지만 검색용으로 더 받아갈 수 있다 */
    static final int MAX_LIMIT = 50;
    static final int DEFAULT_LIMIT = 8;

    private final GroupTravelPlanRepository groupTravelPlanRepository;

    @Transactional(readOnly = true)
    public List<PopularCityResponseDto> getPopularCities(Integer limit) {
        return groupTravelPlanRepository.findPopularCities(
                PageRequest.of(0, normalizeLimit(limit)));
    }

    // 0 이하나 지나치게 큰 값이 오면 기본값·최대치로 맞춘다.
    // limit 을 그대로 넘기면 한 번의 호출로 표 전체를 긁어갈 수 있다.
    static int normalizeLimit(Integer limit) {
        if (limit == null || limit <= 0) {
            return DEFAULT_LIMIT;
        }
        return Math.min(limit, MAX_LIMIT);
    }
}
