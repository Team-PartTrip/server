package com.example.PartTrip.guardian.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/** 보호자 API 요청 · 응답 (#159) */
public final class GuardianDtos {

    private GuardianDtos() {
    }

    public record InviteResponse(String code, LocalDateTime expiresAt) {}

    public record AcceptRequest(@NotBlank(message = "초대 코드를 입력해주세요.") String code) {}

    /** 연결된 상대. 시니어가 보면 보호자, 보호자가 보면 시니어다 */
    public record LinkResponse(Long linkId, String userId, String nickName, LocalDateTime linkedAt) {}
}
