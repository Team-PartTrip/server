package com.example.PartTrip.main.service;

import org.junit.jupiter.api.Test;

import static com.example.PartTrip.main.service.PopularCityService.DEFAULT_LIMIT;
import static com.example.PartTrip.main.service.PopularCityService.MAX_LIMIT;
import static com.example.PartTrip.main.service.PopularCityService.normalizeLimit;
import static org.junit.jupiter.api.Assertions.assertEquals;

// limit 을 그대로 페이지 크기로 넘기면 한 번의 호출로 표 전체를 긁어갈 수 있다.
class PopularCityServiceTest {

    @Test
    void 값이_없으면_기본값을_쓴다() {
        assertEquals(DEFAULT_LIMIT, normalizeLimit(null));
    }

    @Test
    void 영이하는_기본값으로_돌린다() {
        // PageRequest.of 는 크기가 0 이하면 예외를 던진다
        assertEquals(DEFAULT_LIMIT, normalizeLimit(0));
        assertEquals(DEFAULT_LIMIT, normalizeLimit(-5));
    }

    @Test
    void 최대치를_넘기지_않는다() {
        assertEquals(MAX_LIMIT, normalizeLimit(9999));
        assertEquals(MAX_LIMIT, normalizeLimit(MAX_LIMIT));
    }

    @Test
    void 범위_안의_값은_그대로_쓴다() {
        assertEquals(4, normalizeLimit(4));
    }
}
