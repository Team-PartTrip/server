package com.example.PartTrip.guardian.service;

import com.example.PartTrip.global.exception.ForbiddenException;
import com.example.PartTrip.global.exception.NotFoundException;
import com.example.PartTrip.guardian.dto.GuardianDtos.InviteResponse;
import com.example.PartTrip.guardian.dto.GuardianDtos.LinkResponse;
import com.example.PartTrip.guardian.entity.GuardianInviteEntity;
import com.example.PartTrip.guardian.entity.GuardianLinkEntity;
import com.example.PartTrip.guardian.repository.GuardianInviteRepository;
import com.example.PartTrip.guardian.repository.GuardianLinkRepository;
import com.example.PartTrip.planner.dto.response.PlannerListResponseDto;
import com.example.PartTrip.planner.dto.response.PlannerScheduleResponseDto;
import com.example.PartTrip.planner.service.PlannerDraftService;
import com.example.PartTrip.planner.service.PlannerListService;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.signup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class GuardianService {

    static final int INVITE_HOURS = 24;
    // 헷갈리는 글자(0 · O · 1 · I · L)를 뺐다. 어르신이 불러주고 받아 적는다
    private static final String CODE_CHARS = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final GuardianInviteRepository inviteRepository;
    private final GuardianLinkRepository linkRepository;
    private final UserRepository userRepository;
    private final PlannerListService plannerListService;
    private final PlannerDraftService plannerDraftService;

    /** 시니어가 보호자 초대 코드를 만든다. 24시간 동안 한 번 쓸 수 있다 */
    @Transactional
    public InviteResponse createInvite(String seniorUserId) {
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(INVITE_HOURS);
        for (int attempt = 0; attempt < 10; attempt++) {
            String code = randomCode();
            if (!inviteRepository.existsById(code)) {
                inviteRepository.save(new GuardianInviteEntity(code, seniorUserId, expiresAt));
                return new InviteResponse(code, expiresAt);
            }
        }
        throw new IllegalStateException("초대 코드를 만들지 못했습니다.");
    }

    /** 보호자가 코드를 넣어 연결한다. 이미 연결돼 있으면 그 연결을 돌려준다 */
    @Transactional
    public LinkResponse accept(String code, String guardianUserId) {
        GuardianInviteEntity invite = inviteRepository
                .findByCodeForUpdate(code.trim().toUpperCase(Locale.ROOT))
                .orElseThrow(() -> new IllegalArgumentException("초대 코드가 맞지 않아요. 다시 확인해주세요."));
        if (invite.getExpiresAt().isBefore(LocalDateTime.now())) {
            inviteRepository.delete(invite);
            throw new IllegalArgumentException("초대 코드가 만료됐어요. 새 코드를 받아주세요.");
        }
        String seniorUserId = invite.getSeniorUserId();
        if (seniorUserId.equals(guardianUserId)) {
            throw new IllegalArgumentException("내 초대 코드로는 연결할 수 없어요.");
        }

        GuardianLinkEntity link = linkRepository
                .findBySeniorUserIdAndGuardianUserId(seniorUserId, guardianUserId)
                .orElseGet(() -> linkRepository.save(
                        new GuardianLinkEntity(seniorUserId, guardianUserId, LocalDateTime.now())));
        inviteRepository.delete(invite);
        return toResponse(link, seniorUserId);
    }

    /** 내가 보호하는 시니어들 */
    @Transactional(readOnly = true)
    public List<LinkResponse> getSeniors(String guardianUserId) {
        return linkRepository.findByGuardianUserIdOrderByLinkedAtAsc(guardianUserId).stream()
                .map(link -> toResponse(link, link.getSeniorUserId()))
                .toList();
    }

    /** 나를 보호하는 사람들. 시니어가 연결을 끊을 때 본다 */
    @Transactional(readOnly = true)
    public List<LinkResponse> getGuardians(String seniorUserId) {
        return linkRepository.findBySeniorUserIdOrderByLinkedAtAsc(seniorUserId).stream()
                .map(link -> toResponse(link, link.getGuardianUserId()))
                .toList();
    }

    /** 연결 끊기. 시니어와 보호자 둘 다 끊을 수 있다 */
    @Transactional
    public void unlink(Long linkId, String userId) {
        GuardianLinkEntity link = linkRepository.findById(linkId)
                .orElseThrow(() -> new NotFoundException("연결을 찾을 수 없습니다."));
        if (!userId.equals(link.getSeniorUserId()) && !userId.equals(link.getGuardianUserId())) {
            throw new ForbiddenException("내 연결만 끊을 수 있습니다.");
        }
        linkRepository.delete(link);
    }

    /** 시니어의 플래너 목록 */
    @Transactional(readOnly = true)
    public List<PlannerListResponseDto> getSeniorPlanners(String seniorUserId, String guardianUserId) {
        requireLink(seniorUserId, guardianUserId);
        return plannerListService.getMyPlanners(seniorUserId);
    }

    /** 시니어의 일정 카드. 시니어가 그 플래너의 멤버가 아니면 기존 조회가 403 을 낸다 */
    @Transactional(readOnly = true)
    public PlannerScheduleResponseDto getSeniorSchedule(
            String seniorUserId, Long plannerId, String guardianUserId) {
        requireLink(seniorUserId, guardianUserId);
        return plannerDraftService.getSchedule(plannerId, seniorUserId);
    }

    /** 연결된 보호자인지. 위치 공유(#160)도 이걸로 막는다 */
    public void requireLink(String seniorUserId, String guardianUserId) {
        if (!linkRepository.existsBySeniorUserIdAndGuardianUserId(seniorUserId, guardianUserId)) {
            throw new ForbiddenException("연결된 보호자만 볼 수 있습니다.");
        }
    }

    private LinkResponse toResponse(GuardianLinkEntity link, String otherUserId) {
        String nickName = userRepository.findByUserId(otherUserId)
                .map(UserEntity::getNickName)
                .orElse("알 수 없는 사용자");
        return new LinkResponse(link.getLinkId(), otherUserId, nickName, link.getLinkedAt());
    }

    private static String randomCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CODE_CHARS.charAt(RANDOM.nextInt(CODE_CHARS.length())));
        }
        return code.toString();
    }
}
