package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.response.PlannerListResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * 플래너 목록은 쿼리 한 번으로 받는다.
 *
 * 그룹에 계획이 여러 개면 행도 여러 개 온다. 쿼리가 계획 최신순으로 정렬해
 * 주고 서비스는 그룹별 첫 행만 쓰는데, 그 약속이 깨지면 목록에 옛 여행지가
 * 뜬다. 화면만 봐서는 알아채기 어려워 여기서 잡는다.
 */
@ExtendWith(MockitoExtension.class)
class PlannerListServiceTest {

    private static final String USER_ID = "chanwoo";

    @Mock
    private GroupMemberRepository groupMemberRepository;

    @InjectMocks
    private PlannerListService plannerListService;

    private Object[] row(long groupId, String title, GroupTravelPlanEntity plan, long joined) {
        GroupMemberEntity m = new GroupMemberEntity();
        m.setGroupId(groupId);
        m.setUserId(USER_ID);
        m.setRole(GroupRole.OWNER);

        TravelGroupEntity g = new TravelGroupEntity();
        g.setGroupId(groupId);
        g.setGroupName(title);
        g.setStatus(GroupStatus.PLANNING);
        g.setHeadcount(4);
        g.setCreatedAt(LocalDateTime.of(2026, 9, 1, 0, 0));

        return new Object[] {m, g, plan, joined};
    }

    private GroupTravelPlanEntity plan(String city, LocalDate start) {
        GroupTravelPlanEntity p = new GroupTravelPlanEntity();
        p.setCityName(city);
        p.setCountryName("일본");
        p.setStartDate(start);
        p.setEndDate(start.plusDays(3));
        return p;
    }

    @Test
    void 계획이_여러개면_첫_행만_쓴다() {
        // 쿼리가 최신순으로 주므로 오사카가 먼저 온다
        given(groupMemberRepository.findMyPlannerRows(USER_ID)).willReturn(List.of(
                row(1L, "여행", plan("오사카", LocalDate.of(2026, 9, 10)), 2L),
                row(1L, "여행", plan("후쿠오카", LocalDate.of(2025, 5, 2)), 2L)));

        List<PlannerListResponseDto> result = plannerListService.getMyPlanners(USER_ID);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCityName()).isEqualTo("오사카");
    }

    @Test
    void 계획이_없으면_여행지가_비어_온다() {
        given(groupMemberRepository.findMyPlannerRows(USER_ID))
                .willReturn(List.<Object[]>of(row(1L, "이름만 만든 플래너", null, 1L)));

        PlannerListResponseDto only = plannerListService.getMyPlanners(USER_ID).get(0);

        assertThat(only.getTitle()).isEqualTo("이름만 만든 플래너");
        assertThat(only.getCityName()).isNull();
        assertThat(only.getStartDate()).isNull();
        assertThat(only.getJoinedMemberCount()).isEqualTo(1);
    }

    @Test
    void 쿼리가_준_순서를_그대로_지킨다() {
        given(groupMemberRepository.findMyPlannerRows(USER_ID)).willReturn(List.of(
                row(3L, "최근", null, 1L),
                row(2L, "중간", null, 1L),
                row(1L, "오래된", null, 1L)));

        assertThat(plannerListService.getMyPlanners(USER_ID))
                .extracting(PlannerListResponseDto::getTitle)
                .containsExactly("최근", "중간", "오래된");
    }

    @Test
    void 속한_그룹이_없으면_빈_목록() {
        given(groupMemberRepository.findMyPlannerRows(USER_ID)).willReturn(List.<Object[]>of());

        assertThat(plannerListService.getMyPlanners(USER_ID)).isEmpty();
    }
}
