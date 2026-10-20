package com.example.PartTrip.main.repository;

import com.example.PartTrip.main.entity.FestivalEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FestivalRepositoryTest {

    @Autowired private FestivalRepository festivalRepository;

    private void save(String title, String start, String end) {
        FestivalEntity festival = new FestivalEntity();
        festival.setCountryName("대한민국");
        festival.setTitle(title);
        festival.setCategory("축제");
        festival.setDescription("");
        festival.setLocation("강원");
        festival.setStartDate(start);
        festival.setEndDate(end);
        festivalRepository.save(festival);
    }

    @Test
    void 그_달에_시작하거나_이어지는_축제만_나온다() {
        save("9월에 시작해 10월까지", "2026-09-25", "2026-10-05");
        save("10월에 시작", "2026-10-10", "2026-10-12");
        save("끝나는 날 없는 10월 행사", "2026-10-31", null);
        save("9월에 끝남", "2026-09-01", "2026-09-30");
        save("11월 시작", "2026-11-01", "2026-11-03");
        save("끝나는 날 없는 9월 행사", "2026-09-15", null);

        assertThat(festivalRepository.findInMonth("대한민국", "2026-10-01", "2026-10-31"))
                .extracting(FestivalEntity::getTitle)
                .containsExactly("9월에 시작해 10월까지", "10월에 시작", "끝나는 날 없는 10월 행사");
    }
}
