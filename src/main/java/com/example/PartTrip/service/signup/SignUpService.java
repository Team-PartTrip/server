package com.example.PartTrip.service.signup;

import com.example.PartTrip.dto.signup.SignUpRequestDto;
import com.example.PartTrip.entity.UserEntity;
import com.example.PartTrip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public UserEntity saveUser(SignUpRequestDto dto) {

        if (userRepository.existsByUserId(dto.getUserId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        if (!mailService.isVerified(dto.getUserMail())) {
            throw new IllegalArgumentException("이메일 인증이 필요합니다.");
        }

        UserEntity user = new UserEntity();

        user.setUserId(dto.getUserId());
        user.setUserPwd(passwordEncoder.encode(dto.getUserPwd()));
        user.setUserMail(dto.getUserMail());
        user.setSignUpDivision(dto.getSignUpDivision());
        user.setNickName("사용자 " + (userRepository.count() + 1));
        user.setMyCountry(dto.getMyCountry());
        user.setCreateDate(LocalDateTime.now());

        return userRepository.save(user);
    }
}