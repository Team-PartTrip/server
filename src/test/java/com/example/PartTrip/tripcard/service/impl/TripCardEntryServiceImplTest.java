package com.example.PartTrip.tripcard.service.impl;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// 촬영 시각을 모르는 사진은 takenAt 이 null 이다. 순번을 매길 때 이 사진들끼리
// 한 묶음이 되어야 하고, 날짜가 있는 사진 묶음에 섞이면 안 된다.
class TripCardEntryServiceImplTest {

    @Test
    void 같은_날짜면_한_묶음이다() {
        LocalDateTime morning = LocalDateTime.of(2026, 8, 15, 9, 0);
        LocalDateTime evening = LocalDateTime.of(2026, 8, 15, 21, 0);

        assertTrue(TripCardEntryServiceImpl.sameDate(morning, LocalDate.of(2026, 8, 15)));
        assertTrue(TripCardEntryServiceImpl.sameDate(evening, LocalDate.of(2026, 8, 15)));
        assertFalse(TripCardEntryServiceImpl.sameDate(morning, LocalDate.of(2026, 8, 16)));
    }

    @Test
    void 날짜를_모르는_사진끼리_한_묶음이다() {
        assertTrue(TripCardEntryServiceImpl.sameDate(null, null));
    }

    @Test
    void 날짜를_모르는_사진은_날짜_있는_묶음에_안_섞인다() {
        LocalDate day = LocalDate.of(2026, 8, 15);

        assertFalse(TripCardEntryServiceImpl.sameDate(null, day));
        assertFalse(TripCardEntryServiceImpl.sameDate(LocalDateTime.of(2026, 8, 15, 9, 0), null));
    }
}
