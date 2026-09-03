package com.example.PartTrip.password.service;

import com.example.PartTrip.password.dto.PasswordResetRequestDto;
import com.example.PartTrip.signup.dto.EmailVerifyRequestDto;
import com.example.PartTrip.signup.entity.EmailVerificationEntity;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.signup.repository.EmailVerificationRepository;
import com.example.PartTrip.signup.repository.UserRepository;
import com.example.PartTrip.signup.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindPasswordService {

    private final UserRepository userRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationRepository emailVerificationRepository;

    // 1단계: 가입된 이메일인지 확인 후 인증번호 발송
    public void sendResetCode(String email) {

        String normalizedEmail = normalizeEmail(email);

        // 가입되지 않은 이메일이면 비밀번호를 찾을 수 없음
        findUniqueUserByEmail(normalizedEmail);

        // 기존 이메일 인증 로직 재활용
        mailService.sendCode(normalizedEmail);
    }

    /**
     * 2단계: 이메일로 받은 인증번호 확인.
     *
     * 인증에 성공한 쪽에만 일회용 토큰을 돌려준다. 3단계는 이 토큰을 다시
     * 받아, 인증한 사람과 비밀번호를 바꾸는 사람이 같은지 확인한다.
     *
     * @return 재설정에 쓸 일회용 토큰
     */
    @Transactional
    public String verifyResetCode(EmailVerifyRequestDto dto) {

        // 기존 인증번호 검증 로직 재활용 (인증 성공 시 verified = true 로 저장됨)
        mailService.verifyCode(dto);

        String normalizedEmail = normalizeEmail(dto.getEmail());
        EmailVerificationEntity verification = emailVerificationRepository
                .findById(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException("인증번호를 먼저 요청해주세요."));

        String token = UUID.randomUUID().toString();
        verification.setResetToken(token);
        emailVerificationRepository.save(verification);
        return token;
    }

    // 3단계: 새 비밀번호로 변경
    // 비밀번호 저장과 인증정보 삭제는 함께 성공하거나 함께 실패해야 한다.
    // 중간에 실패하면 인증정보가 남아 같은 인증번호로 다시 변경할 수 있다.
    @Transactional
    public void resetPassword(PasswordResetRequestDto dto) {

        String normalizedEmail = normalizeEmail(dto.getEmail());

        // 새 비밀번호와 재입력한 비밀번호가 일치하는지 확인
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 이메일 인증을 먼저 완료했는지 확인
        if (!mailService.isVerified(normalizedEmail)) {
            throw new IllegalArgumentException("이메일 인증을 먼저 완료해주세요.");
        }

        // 인증을 마친 그 사람이 맞는지 확인한다. 이메일만 보면, 피해자가
        // 인증을 끝낸 사이에 이메일만 아는 사람도 비밀번호를 바꿀 수 있다.
        EmailVerificationEntity verification = emailVerificationRepository
                .findById(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException("이메일 인증을 먼저 완료해주세요."));
        String issued = verification.getResetToken();
        if (issued == null
                || !MessageDigest.isEqual(
                        issued.getBytes(StandardCharsets.UTF_8),
                        dto.getResetToken().getBytes(StandardCharsets.UTF_8))) {
            throw new IllegalArgumentException("이메일 인증을 먼저 완료해주세요.");
        }

        // 비밀번호를 변경할 회원 조회
        UserEntity user = findUniqueUserByEmail(normalizedEmail);

        // 새 비밀번호를 암호화해서 저장
        user.setUserPwd(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        // 사용이 끝난 인증 정보 삭제 (재사용 방지)
        emailVerificationRepository.deleteById(normalizedEmail);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private UserEntity findUniqueUserByEmail(String email) {
        List<UserEntity> users = userRepository
                .findAllByUserMailIgnoreCaseOrderByUserIdAsc(email);
        if (users.isEmpty()) {
            throw new IllegalArgumentException("가입되지 않은 이메일입니다.");
        }
        if (users.size() > 1) {
            throw new IllegalArgumentException("이메일에 연결된 계정이 여러 개입니다.");
        }
        return users.get(0);
    }
}
