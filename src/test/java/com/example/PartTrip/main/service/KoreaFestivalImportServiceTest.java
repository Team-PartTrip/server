package com.example.PartTrip.main.service;

import com.example.PartTrip.main.entity.FestivalEntity;
import com.example.PartTrip.main.repository.FestivalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KoreaFestivalImportServiceTest {

    private static final ObjectMapper JSON = new ObjectMapper();

    @Mock FestivalRepository festivalRepository;
    @Spy ObjectMapper objectMapper = new ObjectMapper();
    @InjectMocks KoreaFestivalImportService service;

    private static final String PAGE = """
            {"response":{"header":{"resultCode":"0000"},"body":{"items":{"item":[
              {"contentid":"2786391","title":"강릉 커피축제","eventstartdate":"20261008","eventenddate":"20261011",
               "addr1":"강원특별자치도 강릉시 창해로 17","firstimage":"http://tong.visitkorea.or.kr/cms/a.jpg","cat2":"","lclsSystm2":"EV01"},
              {"contentid":"2786392","title":"하루 공연","eventstartdate":"20261020","eventenddate":"20261020",
               "addr1":"","firstimage":"","cat2":"","lclsSystm2":"EV02"},
              {"contentid":"2786393","title":"1년 내내 하는 관람","eventstartdate":"20260101","eventenddate":"20261231",
               "lclsSystm2":"EV03"},
              {"contentid":"","title":"아이디 없음","eventstartdate":"20261020"}
            ]},"numOfRows":100,"pageNo":1,"totalCount":3}}}
            """;

    @Test
    void 행사정보를_축제로_바꾼다() throws Exception {
        List<FestivalEntity> festivals = KoreaFestivalImportService.parse(JSON.readTree(PAGE));

        // 아이디가 없는 행사는 덮어쓸 기준이 없어서, 60일 넘는 상설 행사는 축제가 아니라서 버린다
        assertThat(festivals).extracting(FestivalEntity::getTitle).containsExactly("강릉 커피축제", "하루 공연");
        FestivalEntity coffee = festivals.get(0);
        assertThat(coffee.getSourceId()).isEqualTo("2786391");
        assertThat(coffee.getCountryName()).isEqualTo("대한민국");
        assertThat(coffee.getStartDate()).isEqualTo("2026-10-08");
        assertThat(coffee.getEndDate()).isEqualTo("2026-10-11");
        assertThat(coffee.getCategory()).isEqualTo("축제");
        assertThat(coffee.getDescription()).isEqualTo("기간 2026.10.08 ~ 2026.10.11");
        // iOS 는 http 사진을 막는다
        assertThat(coffee.getImageUrl()).isEqualTo("https://tong.visitkorea.or.kr/cms/a.jpg");

        FestivalEntity show = festivals.get(1);
        assertThat(show.getCategory()).isEqualTo("공연");
        assertThat(show.getDescription()).isEqualTo("기간 2026.10.20");
        assertThat(show.getLocation()).isEqualTo("장소 정보 없음");
        assertThat(show.getImageUrl()).isNull();
    }

    @Test
    void 하나뿐이면_객체로_없으면_빈_문자열로_온다() throws Exception {
        assertThat(KoreaFestivalImportService.parse(JSON.readTree("""
                {"response":{"body":{"items":{"item":{"contentid":"1","title":"하나","eventstartdate":"20261001"}}}}}
                """))).hasSize(1);
        assertThat(KoreaFestivalImportService.parse(JSON.readTree("""
                {"response":{"body":{"items":"","totalCount":0}}}
                """))).isEmpty();
    }

    @Test
    void 이미_받은_축제는_덮어쓴다() throws Exception {
        FestivalEntity old = new FestivalEntity();
        old.setFestivalId(5L);
        old.setSourceId("2786391");
        old.setTitle("옛 이름");
        given(festivalRepository.findBySourceIdIn(any())).willReturn(List.of(old));
        given(festivalRepository.saveAll(any())).willAnswer(inv -> inv.getArgument(0));

        service.upsert(KoreaFestivalImportService.parse(JSON.readTree(PAGE)));

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<FestivalEntity>> saved = ArgumentCaptor.forClass(List.class);
        verify(festivalRepository).saveAll(saved.capture());
        // 같은 행(id 5)을 고쳐 쓴다. 새로 만들면 받을 때마다 축제가 늘어난다
        assertThat(saved.getValue().get(0).getFestivalId()).isEqualTo(5L);
        assertThat(saved.getValue().get(0).getTitle()).isEqualTo("강릉 커피축제");
        assertThat(saved.getValue().get(1).getFestivalId()).isNull();
    }

    @Test
    void 인증키가_없으면_부르지_않는다() {
        assertThat(service.importFrom(LocalDate.of(2026, 9, 1))).isZero();
        verify(festivalRepository, never()).saveAll(any());
    }

    @Test
    void 날짜_형식이_틀리면_버린다() {
        assertThat(KoreaFestivalImportService.date("2026-10-01")).isNull();
        assertThat(KoreaFestivalImportService.date(null)).isNull();
        assertThat(KoreaFestivalImportService.date("20261001")).isEqualTo("2026-10-01");
    }

    @Test
    void 분류는_새_분류를_먼저_보고_없으면_옛_분류를_본다() {
        assertThat(KoreaFestivalImportService.categoryOf("EV01", null)).isEqualTo("축제");
        assertThat(KoreaFestivalImportService.categoryOf("EV02", "A0207")).isEqualTo("공연");
        assertThat(KoreaFestivalImportService.categoryOf("EV03", null)).isEqualTo("행사");
        assertThat(KoreaFestivalImportService.categoryOf(null, "A0207")).isEqualTo("축제");
        assertThat(KoreaFestivalImportService.categoryOf(null, null)).isEqualTo("행사");
    }
}
