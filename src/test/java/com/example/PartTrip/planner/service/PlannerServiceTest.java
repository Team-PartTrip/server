package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.request.CreatePlannerRequestDto;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PlannerServiceTest {

    private static final String USER_ID = "user";

    @Mock private TravelGroupRepository travelGroupRepository;
    @Mock private GroupMemberRepository groupMemberRepository;
    @Mock private GroupTravelPlanRepository groupTravelPlanRepository;
    @Mock private PlannerInviteLinkFactory inviteLinkFactory;
    @InjectMocks private PlannerService plannerService;

    @Test
    void rejectsPlannerCreationWhenTravelDatesOverlapExistingPlan() {
        CreatePlannerRequestDto request = request();
        given(groupTravelPlanRepository.existsOverlappingPlanForUser(
                USER_ID, request.getStartDate(), request.getEndDate()))
                .willReturn(true);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> plannerService.createPlanner(request, USER_ID))
                .withMessage("해당 기간에 이미 등록된 여행 계획이 있습니다.");

        verify(travelGroupRepository, never()).save(any());
        verify(groupMemberRepository, never()).save(any());
        verify(groupTravelPlanRepository, never()).save(any());
    }

    @Test
    void plannerWithoutTravelDatesDoesNotRunOverlapQuery() {
        CreatePlannerRequestDto request = new CreatePlannerRequestDto();
        request.setTitle("나중에 정할 여행");
        request.setMemberCount(2);
        request.setIsSolo(false);

        given(travelGroupRepository.save(any())).willAnswer(invocation -> {
            com.example.PartTrip.planner.entity.TravelGroupEntity group = invocation.getArgument(0);
            group.setGroupId(1L);
            return group;
        });
        given(travelGroupRepository.existsByInviteCode(any())).willReturn(false);

        plannerService.createPlanner(request, USER_ID);

        verify(groupTravelPlanRepository, never())
                .existsOverlappingPlanForUser(any(), any(), any());
    }

    private CreatePlannerRequestDto request() {
        CreatePlannerRequestDto request = new CreatePlannerRequestDto();
        request.setTitle("싱가포르 여행");
        request.setMemberCount(2);
        request.setIsSolo(false);
        request.setCountryName("싱가포르");
        request.setCityName("싱가포르");
        request.setStartDate(LocalDate.of(2026, 9, 1));
        request.setEndDate(LocalDate.of(2026, 9, 30));
        return request;
    }

}
