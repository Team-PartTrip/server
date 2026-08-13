package com.example.PartTrip.application.user;

import com.example.PartTrip.domain.signup.entity.UserEntity;
import com.example.PartTrip.domain.signup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    // 여행 취향 설문 완료 처리 (최초 로그인 시 설문 화면 노출 여부 판단용)
    public void completeSurvey(String userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        user.setSurveyCompleted(true);
    }
}
