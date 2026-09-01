package com.example.PartTrip.login.service;

import com.example.PartTrip.global.exception.RefreshTokenReuseException;
import com.example.PartTrip.global.security.JwtUtil;
import com.example.PartTrip.login.dto.LogoutRequestDto;
import com.example.PartTrip.login.dto.RefreshRequestDto;
import com.example.PartTrip.login.dto.TokenResponseDto;
import com.example.PartTrip.login.entity.RefreshTokenEntity;
import com.example.PartTrip.login.repository.RefreshTokenRepository;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.signup.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * 리프레시 토큰 회전 (#103).
 *
 * 토큰은 쓸 때마다 새것으로 바뀌고 옛것은 죽는다. 다만 갱신 응답이 유실되면
 * 앱은 옛 토큰을 그대로 들고 있어 다시 보내므로, 짧은 유예 동안은 받아준다.
 * 그 선을 잘못 그으면 멀쩡한 사용자가 쫓겨나거나, 샌 토큰이 계속 살아 있다.
 */
@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    private static final String USER_ID = "chanwoo219";
    private static final String USER_MAIL = "a@b.c";
    private static final String OLD_TOKEN = "old-refresh-token";
    private static final String NEW_TOKEN = "new-refresh-token";

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Mock private RefreshTokenRepository refreshTokenRepository;
    @InjectMocks private LoginService loginService;

    private RefreshTokenEntity stored;

    @BeforeEach
    void setUp() {
        stored = new RefreshTokenEntity();
        stored.setUserId(USER_ID);
        stored.setRefreshToken(OLD_TOKEN);
        stored.setExpiredAt(LocalDateTime.now().plusDays(7));
        stored.setCreateDate(LocalDateTime.now());

        lenient().when(jwtUtil.getUserId(anyString())).thenReturn(USER_ID);
        lenient().when(jwtUtil.getUserMail(anyString())).thenReturn(USER_MAIL);
        lenient().when(jwtUtil.isExpired(anyString())).thenReturn(false);
        lenient().when(jwtUtil.createAccessToken(anyString(), anyString()))
                .thenReturn("access");
        lenient().when(jwtUtil.createRefreshToken(anyString(), anyString()))
                .thenReturn(NEW_TOKEN);
        lenient().when(userRepository.findById(USER_ID))
                .thenReturn(Optional.of(new UserEntity()));
    }

    private TokenResponseDto refresh(String token) {
        RefreshRequestDto dto = new RefreshRequestDto();
        dto.setRefreshToken(token);
        return loginService.refresh(dto);
    }

    @Test
    void 갱신하면_리프레시_토큰이_새것으로_바뀐다() {
        given(refreshTokenRepository.findByRefreshTokenForUpdate(OLD_TOKEN))
                .willReturn(Optional.of(stored));

        TokenResponseDto result = refresh(OLD_TOKEN);

        assertThat(result.getRefreshToken()).isEqualTo(NEW_TOKEN);
        assertThat(stored.getRefreshToken()).isEqualTo(NEW_TOKEN);
        // 옛 토큰은 유예 동안만 살아 있는다
        assertThat(stored.getPreviousToken()).isEqualTo(OLD_TOKEN);
        assertThat(stored.getPreviousValidUntil()).isAfter(LocalDateTime.now());
        verify(refreshTokenRepository).save(stored);
    }

    @Test
    void 응답이_유실돼_옛_토큰으로_다시_물어보면_같은_토큰을_준다() {
        // 유예 안. 여기서 쫓아내면 한 번 끊긴 것만으로 로그아웃된다.
        stored.setRefreshToken(NEW_TOKEN);
        stored.setPreviousToken(OLD_TOKEN);
        stored.setPreviousValidUntil(LocalDateTime.now().plusSeconds(30));
        given(refreshTokenRepository.findByRefreshTokenForUpdate(OLD_TOKEN))
                .willReturn(Optional.empty());
        given(refreshTokenRepository.findByPreviousToken(OLD_TOKEN))
                .willReturn(Optional.of(stored));

        TokenResponseDto result = refresh(OLD_TOKEN);

        assertThat(result.getRefreshToken()).isEqualTo(NEW_TOKEN);
        // 다시 회전시키면 안 된다
        verify(refreshTokenRepository, never()).save(any());
        verify(refreshTokenRepository, never()).delete(any());
    }

    @Test
    void 유예가_지난_옛_토큰은_재사용으로_보고_세션을_끊는다() {
        stored.setRefreshToken(NEW_TOKEN);
        stored.setPreviousToken(OLD_TOKEN);
        stored.setPreviousValidUntil(LocalDateTime.now().minusSeconds(1));
        given(refreshTokenRepository.findByRefreshTokenForUpdate(OLD_TOKEN))
                .willReturn(Optional.empty());
        given(refreshTokenRepository.findByPreviousToken(OLD_TOKEN))
                .willReturn(Optional.of(stored));

        // 전용 예외여야 한다. IllegalArgumentException 이면 트랜잭션이 롤백해
        // 바로 위의 delete 가 없던 일이 되고, 탈취된 세션이 살아남는다.
        assertThatThrownBy(() -> refresh(OLD_TOKEN))
                .isInstanceOf(RefreshTokenReuseException.class)
                .hasMessageContaining("이미 사용된");

        verify(refreshTokenRepository).delete(stored);
    }

    @Test
    void 모르는_토큰은_거부한다() {
        given(refreshTokenRepository.findByRefreshTokenForUpdate("unknown"))
                .willReturn(Optional.empty());
        given(refreshTokenRepository.findByPreviousToken("unknown"))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> refresh("unknown"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는");
    }

    @Test
    void 저장된_만료시각이_지났으면_거부한다() {
        stored.setExpiredAt(LocalDateTime.now().minusMinutes(1));
        given(refreshTokenRepository.findByRefreshTokenForUpdate(OLD_TOKEN))
                .willReturn(Optional.of(stored));

        assertThatThrownBy(() -> refresh(OLD_TOKEN))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("만료");
    }

    @Test
    void 빈_토큰은_조회도_하지_않는다() {
        assertThatThrownBy(() -> refresh("  "))
                .isInstanceOf(IllegalArgumentException.class);
        verify(refreshTokenRepository, never()).findByRefreshTokenForUpdate(anyString());
    }

    @Test
    void 갱신_직후_로그아웃에_옛_토큰을_내밀어도_끊어준다() {
        stored.setRefreshToken(NEW_TOKEN);
        stored.setPreviousToken(OLD_TOKEN);
        given(refreshTokenRepository.findByRefreshToken(OLD_TOKEN))
                .willReturn(Optional.empty());
        given(refreshTokenRepository.findByPreviousToken(OLD_TOKEN))
                .willReturn(Optional.of(stored));

        LogoutRequestDto dto = new LogoutRequestDto();
        dto.setRefreshToken(OLD_TOKEN);
        loginService.logout(dto);

        verify(refreshTokenRepository).delete(stored);
    }
}
