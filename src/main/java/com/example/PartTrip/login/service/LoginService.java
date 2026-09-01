package com.example.PartTrip.login.service;

import com.example.PartTrip.login.dto.LoginRequestDto;
import com.example.PartTrip.login.dto.LogoutRequestDto;
import com.example.PartTrip.login.dto.RefreshRequestDto;
import com.example.PartTrip.login.dto.TokenResponseDto;
import com.example.PartTrip.login.entity.RefreshTokenEntity;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.global.exception.RefreshTokenReuseException;
import com.example.PartTrip.global.security.JwtUtil;
import com.example.PartTrip.login.repository.RefreshTokenRepository;
import com.example.PartTrip.signup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    // 로그인 처리
    public TokenResponseDto login(LoginRequestDto dto) {

        // 1. 사용자가 입력한 아이디로 DB에서 회원 찾기
        UserEntity user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (!passwordEncoder.matches(dto.getUserPwd(), user.getUserPwd())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // AccessToken 생성
        String accessToken = jwtUtil.createAccessToken(
                user.getUserId(),
                user.getUserMail()
        );

        // RefreshToken 생성
        String refreshToken = jwtUtil.createRefreshToken(
                user.getUserId(),
                user.getUserMail()
        );

        // 사용자의 Refrsh token이 DB에 있는지 확인
        RefreshTokenEntity tokenEntity = refreshTokenRepository.findByUserId(user.getUserId())
                .orElse(new RefreshTokenEntity());

        // Refresh token 정보 저장
        tokenEntity.setUserId(user.getUserId());
        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setExpiredAt(LocalDateTime.now().plusDays(7));
        tokenEntity.setCreateDate(LocalDateTime.now());
        // 새 세션이다. 옛 세션의 유예 토큰이 남아 있으면 그걸로도 갱신이 되므로 지운다.
        tokenEntity.setPreviousToken(null);
        tokenEntity.setPreviousValidUntil(null);

        refreshTokenRepository.save(tokenEntity);

        return new TokenResponseDto(accessToken, refreshToken);
    }

    // 갱신 응답이 유실됐을 때 앱이 옛 토큰으로 다시 물어봐도 받아주는 시간.
    // 이게 없으면 지하철에서 한 번 끊긴 것만으로 로그아웃된다.
    private static final long REPLAY_GRACE_SECONDS = 60;

    /**
     * 액세스 토큰 갱신.
     *
     * 리프레시 토큰은 쓸 때마다 새것으로 바꾼다(회전). 옛 토큰은 바로 죽는다.
     * 그래야 토큰이 한 번 새도 계속 쓰이지 못한다.
     *
     * 다만 회전 직후 응답이 유실되는 경우가 있다. 그때 앱은 옛 토큰을 그대로
     * 들고 있어 다시 보내는데, 이걸 곧장 "탈취" 로 보면 멀쩡한 사용자가
     * 쫓겨난다. 그래서 짧은 유예 동안은 같은 응답을 다시 준다.
     * 유예가 지난 뒤의 재사용은 탈취로 보고 세션을 끊는다.
     */
    // 재사용을 탐지하면 행을 지운 뒤 예외를 던진다. 클래스의 @Transactional 이
    // 예외에 롤백해 버리면 그 삭제가 없던 일이 되어, 탈취된 세션이 그대로
    // 살아남는다. 이 예외에만 롤백하지 않는다.
    @Transactional(noRollbackFor = RefreshTokenReuseException.class)
    public TokenResponseDto refresh(RefreshRequestDto dto) {

        String presented = dto.getRefreshToken();
        if (presented == null || presented.isBlank()) {
            throw new IllegalArgumentException("Refresh Token 이 필요합니다.");
        }

        // 같은 토큰으로 두 요청이 동시에 오면 서로의 회전 결과를 덮어쓴다.
        // 행을 잠가 한 번에 하나만 지나가게 한다.
        Optional<RefreshTokenEntity> current =
                refreshTokenRepository.findByRefreshTokenForUpdate(presented);

        if (current.isEmpty()) {
            // 지금 토큰이 아니다. 방금 회전시킨 직전 토큰인지 본다.
            RefreshTokenEntity replayed = refreshTokenRepository.findByPreviousToken(presented)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "존재하지 않는 Refresh Token 입니다."));

            if (replayed.getPreviousValidUntil() == null
                    || replayed.getPreviousValidUntil().isBefore(LocalDateTime.now())) {
                // 유예가 끝난 옛 토큰이다. 탈취로 보고 세션을 끊는다.
                refreshTokenRepository.delete(replayed);
                throw new RefreshTokenReuseException(
                        "이미 사용된 Refresh Token 입니다. 다시 로그인 해주세요.");
            }

            // 유예 안이다. 회전을 한 번 더 하지 않고 같은 결과를 돌려준다.
            String userId = jwtUtil.getUserId(replayed.getRefreshToken());
            String userMail = jwtUtil.getUserMail(replayed.getRefreshToken());
            return new TokenResponseDto(
                    jwtUtil.createAccessToken(userId, userMail),
                    replayed.getRefreshToken());
        }

        RefreshTokenEntity tokenEntity = current.get();

        if (tokenEntity.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Refresh Token이 만료되었습니다. 다시 로그인 해주세요.");
        }

        // 서명이 깨졌거나 만료된 토큰이면 JwtException 이 올라가 401 이 된다
        if (jwtUtil.isExpired(presented)) {
            throw new IllegalArgumentException("Refresh Token이 만료되었습니다. 다시 로그인 해주세요.");
        }

        String userId = jwtUtil.getUserId(presented);
        String userMail = jwtUtil.getUserMail(presented);

        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        String newAccessToken = jwtUtil.createAccessToken(userId, userMail);
        String newRefreshToken = jwtUtil.createRefreshToken(userId, userMail);

        tokenEntity.setPreviousToken(presented);
        tokenEntity.setPreviousValidUntil(
                LocalDateTime.now().plusSeconds(REPLAY_GRACE_SECONDS));
        tokenEntity.setRefreshToken(newRefreshToken);
        tokenEntity.setExpiredAt(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(tokenEntity);

        return new TokenResponseDto(newAccessToken, newRefreshToken);
    }

    public String logout(LogoutRequestDto dto) {

        // 갱신 직후 로그아웃하면 앱이 옛 토큰을 내밀 수 있다. 그때도 끊어준다.
        RefreshTokenEntity tokenEntity = refreshTokenRepository
                .findByRefreshToken(dto.getRefreshToken())
                .or(() -> refreshTokenRepository.findByPreviousToken(dto.getRefreshToken()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Refresh Token 입니다."));

        // Refresh Token 삭제
        refreshTokenRepository.delete(tokenEntity);

        return "로그아웃 완료";
    }


}
