package com.example.PartTrip.service;

import com.example.PartTrip.dto.SignUpRequestDto;
import com.example.PartTrip.entity.UserEntity;
import com.example.PartTrip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;

    // 비밀번호 암호화 객체
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public UserEntity saveUser(SignUpRequestDto dto) {

        // 아이디 중복 검사
        if(userRepository.existsByUserId(dto.getUserId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // UserEntity 객체 생성
        UserEntity user = new UserEntity();

        // 로그인 아이디 저장
        user.setUserId(dto.getUserId());

        // 비밀번호 암호화 후 저장
        user.setUserPwd(passwordEncoder.encode(dto.getUserPwd()));

        // 이메일 저장
        user.setUserMail(dto.getUserMail());

        // 닉네임 저장
        user.setNickName(dto.getNickName());

        // 회원가입 방식 저장
        user.setSignUpDivision(dto.getSignUpDivision());

        // 전화번호 저장
        user.setPhnNumber(dto.getPhnNumber());

        // 국가 저장
        user.setMyCountry(dto.getMyCountry());

        // 여행 타입 저장
        user.setTravelType(dto.getTravelType());

        // DB 저장 후 반환
        return userRepository.save(user);
    }
}