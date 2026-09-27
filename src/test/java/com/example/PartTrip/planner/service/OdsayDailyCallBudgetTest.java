package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.repository.OdsayDailyCallUsageRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OdsayDailyCallBudgetTest {

    @Test
    void 저장소의_날짜별_원자_카운터로_한도를_공유한다() {
        OdsayDailyCallUsageRepository repository = mock(OdsayDailyCallUsageRepository.class);
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        when(repository.tryIncrement(today, 2)).thenReturn(1, 1, 0);
        OdsayDailyCallBudget budget = new OdsayDailyCallBudget(repository, 2);

        assertThat(budget.tryAcquire()).isTrue();
        assertThat(budget.tryAcquire()).isTrue();
        assertThat(budget.tryAcquire()).isFalse();

        verify(repository, org.mockito.Mockito.times(3)).tryIncrement(today, 2);
    }
}
