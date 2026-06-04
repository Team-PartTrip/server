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

    // 이메일 인증 성공 후 진짜 회원가입 완료
    public UserEntity completeSignUp(String email) {

        // pending_signup에서 임시 회원가입 정보 조회
        PendingSignUpEntity pending = pendingSignUpRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("회원가입 정보를 먼저 입력해주세요."));

        // 임시 회원가입 시간이 만료됐는지 확인
        if (pending.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("회원가입 시간이 만료되었습니다. 다시 시도해주세요.");
        }

        // 진짜 회원 테이블에 저장할 UserEntity 생성
        UserEntity user = new UserEntity();

        // 임시 저장소에 있던 값을 진짜 회원 Entity에 넣음
        user.setUserId(pending.getUserId());
        user.setUserPwd(pending.getUserPwd());
        user.setUserMail(pending.getUserMail());
        user.setSignUpDivision(pending.getSignupDivision());
        user.setMyCountry(pending.getMyCountry());
        // 닉네임 자동 생성
        user.setNickName("사용자 " + (userRepository.count() + 1));

        // 가입 날짜 저장
        user.setCreateDate(LocalDateTime.now());

        // user_manage 테이블에 진짜 회원 저장
        UserEntity savedUser = userRepository.save(user);

        // 회원가입 완료됐으니 임시 데이터 삭제
        pendingSignUpRepository.delete(pending);

        return savedUser;
    }


}