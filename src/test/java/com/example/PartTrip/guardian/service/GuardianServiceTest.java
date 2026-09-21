package com.example.PartTrip.guardian.service;

import com.example.PartTrip.global.exception.ForbiddenException;
import com.example.PartTrip.guardian.dto.GuardianDtos.LinkResponse;
import com.example.PartTrip.guardian.entity.GuardianInviteEntity;
import com.example.PartTrip.guardian.entity.GuardianLinkEntity;
import com.example.PartTrip.guardian.repository.GuardianInviteRepository;
import com.example.PartTrip.guardian.repository.GuardianLinkRepository;
import com.example.PartTrip.planner.service.PlannerDraftService;
import com.example.PartTrip.planner.service.PlannerListService;
import com.example.PartTrip.signup.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/** 보호자 연결 (#159). 연결 안 된 사람이 시니어 일정을 보면 안 된다 */
@ExtendWith(MockitoExtension.class)
class GuardianServiceTest {

    private static final String SENIOR = "senior";
    private static final String CHILD = "child";

    @Mock private GuardianInviteRepository inviteRepository;
    @Mock private GuardianLinkRepository linkRepository;
    @Mock private UserRepository userRepository;
    @Mock private PlannerListService plannerListService;
    @Mock private PlannerDraftService plannerDraftService;
    @InjectMocks private GuardianService service;

    private void givenInvite(String code, LocalDateTime expiresAt) {
        given(inviteRepository.findByCodeForUpdate(code))
                .willReturn(Optional.of(new GuardianInviteEntity(code, SENIOR, expiresAt)));
    }

    @Test
    void 코드를_만들면_헷갈리는_글자_없이_6자리다() {
        var invite = service.createInvite(SENIOR);

        assertThat(invite.code()).hasSize(6).doesNotContainPattern("[01OIL]");
        assertThat(invite.expiresAt()).isAfter(LocalDateTime.now().plusHours(23));
    }

    @Test
    void 코드로_연결하고_코드는_지운다() {
        givenInvite("ABC234", LocalDateTime.now().plusHours(1));
        given(linkRepository.findBySeniorUserIdAndGuardianUserId(SENIOR, CHILD)).willReturn(Optional.empty());
        given(linkRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        // 받아 적으며 소문자 · 공백이 섞여도 된다
        LinkResponse link = service.accept(" abc234 ", CHILD);

        assertThat(link.userId()).isEqualTo(SENIOR);
        verify(inviteRepository).delete(any(GuardianInviteEntity.class));
    }

    @Test
    void 이미_연결돼_있으면_새로_만들지_않는다() {
        givenInvite("ABC234", LocalDateTime.now().plusHours(1));
        given(linkRepository.findBySeniorUserIdAndGuardianUserId(SENIOR, CHILD))
                .willReturn(Optional.of(new GuardianLinkEntity(SENIOR, CHILD, LocalDateTime.now())));

        service.accept("ABC234", CHILD);

        verify(linkRepository, never()).save(any());
    }

    @Test
    void 만료된_코드는_거부한다() {
        givenInvite("ABC234", LocalDateTime.now().minusMinutes(1));

        assertThatThrownBy(() -> service.accept("ABC234", CHILD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("만료");
        verify(linkRepository, never()).save(any());
    }

    @Test
    void 내_코드로_나를_연결할_수_없다() {
        givenInvite("ABC234", LocalDateTime.now().plusHours(1));

        assertThatThrownBy(() -> service.accept("ABC234", SENIOR))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 연결_안_된_사람은_시니어_일정을_못_본다() {
        given(linkRepository.existsBySeniorUserIdAndGuardianUserId(SENIOR, "stranger")).willReturn(false);

        assertThatThrownBy(() -> service.getSeniorSchedule(SENIOR, 1L, "stranger"))
                .isInstanceOf(ForbiddenException.class);
        verify(plannerDraftService, never()).getSchedule(any(), any());
    }

    @Test
    void 보호자는_시니어_권한으로_일정을_본다() {
        given(linkRepository.existsBySeniorUserIdAndGuardianUserId(SENIOR, CHILD)).willReturn(true);

        service.getSeniorSchedule(SENIOR, 1L, CHILD);

        // 플래너 멤버 확인은 시니어 기준으로 기존 조회가 한다
        verify(plannerDraftService).getSchedule(1L, SENIOR);
    }

    @Test
    void 남의_연결은_끊을_수_없다() {
        given(linkRepository.findById(5L))
                .willReturn(Optional.of(new GuardianLinkEntity(SENIOR, CHILD, LocalDateTime.now())));

        assertThatThrownBy(() -> service.unlink(5L, "stranger"))
                .isInstanceOf(ForbiddenException.class);
        verify(linkRepository, never()).delete(any());
    }

    @Test
    void 시니어도_보호자도_연결을_끊을_수_있다() {
        GuardianLinkEntity link = new GuardianLinkEntity(SENIOR, CHILD, LocalDateTime.now());
        given(linkRepository.findById(5L)).willReturn(Optional.of(link));

        service.unlink(5L, SENIOR);
        service.unlink(5L, CHILD);

        verify(linkRepository, org.mockito.Mockito.times(2)).delete(link);
    }
}
