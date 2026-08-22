package com.example.PartTrip.password.service;

import com.example.PartTrip.password.dto.PasswordResetRequestDto;
import com.example.PartTrip.signup.dto.EmailVerifyRequestDto;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.signup.repository.EmailVerificationRepository;
import com.example.PartTrip.signup.repository.UserRepository;
import com.example.PartTrip.signup.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindPasswordService {

    private final UserRepository userRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationRepository emailVerificationRepository;

    // 1단계: 가입된 이메일인지 확인 후 인증번호 발송
    public void sendResetCode(String email) {

        // 가입되지 않은 이메일이면 비밀번호를 찾을 수 없음
        if (userRepository.findByUserMail(email).isEmpty()) {
            throw new IllegalArgumentException("가입되지 않은 이메일입니다.");
        }

        // 기존 이메일 인증 로직 재활용
        mailService.sendCode(email);
    }

    // 2단계: 이메일로 받은 인증번호 확인
    public void verifyResetCode(EmailVerifyRequestDto dto) {

        // 기존 인증번호 검증 로직 재활용 (인증 성공 시 verified = true 로 저장됨)
        mailService.verifyCode(dto);
    }

    // 3단계: 새 비밀번호로 변경
    // 비밀번호 저장과 인증정보 삭제는 함께 성공하거나 함께 실패해야 한다.
    // 중간에 실패하면 인증정보가 남아 같은 인증번호로 다시 변경할 수 있다.
    @Transactional
    public void resetPassword(PasswordResetRequestDto dto) {

        // 새 비밀번호와 재입력한 비밀번호가 일치하는지 확인
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 이메일 인증을 먼저 완료했는지 확인
        if (!mailService.isVerified(dto.getEmail())) {
            throw new IllegalArgumentException("이메일 인증을 먼저 완료해주세요.");
        }

        // 비밀번호를 변경할 회원 조회
        UserEntity user = userRepository.findByUserMail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        // 새 비밀번호를 암호화해서 저장
        user.setUserPwd(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        // 사용이 끝난 인증 정보 삭제 (재사용 방지)
        emailVerificationRepository.deleteById(dto.getEmail());
    }
}
