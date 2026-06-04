package com.example.PartTrip.service.signup;

import com.example.PartTrip.dto.signup.SignUpRequestDto;
import com.example.PartTrip.entity.signup.PendingSignUpEntity;
import com.example.PartTrip.entity.signup.UserEntity;
import com.example.PartTrip.repository.signup.PendingSignUpRepository;
import com.example.PartTrip.repository.signup.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;
    private final PendingSignUpRepository pendingSignUpRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    // 회원가입 정부 입력 후 이메일 인증번호 발송
    public void startSignUp(SignUpRequestDto dto) {

        // 이미 가입된 아이디인지 검사
        if (userRepository.existsByUserId(dto.getUserId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 임시 회원가입 객체 생성
        PendingSignUpEntity pending = new PendingSignUpEntity();

        // 사용자가 입력한 이메일 저장
        pending.setUserMail(dto.getUserMail());

        // 사용자가 입력한 아이디 저장
        pending.setUserId(dto.getUserId());

        // 비밀번호는 그대로 저장하면 안 됨
        // 암호화해서 임시 저장
        pending.setUserPwd(passwordEncoder.encode(dto.getUserPwd()));

        // 가입 방식 저장
        pending.setSignupDivision(dto.getSignUpDivision());

        // 국가 저장
        pending.setMyCountry(dto.getMyCountry());

        // 10분 안에 인증해야 함
        pending.setExpiredAt(LocalDateTime.now().plusMinutes(10));

        // pending_signup 테이블에 임시 저장
        pendingSignUpRepository.save(pending);

        // 이메일 인증번호 전송
        mailService.sendCode(dto.getUserMail());
    }


}