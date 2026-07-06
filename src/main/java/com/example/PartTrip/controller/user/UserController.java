package com.example.PartTrip.controller.user;

import com.example.PartTrip.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // 여행 취향 설문 완료 처리
    @PostMapping("/survey-complete")
    public String completeSurvey(Authentication authentication) {
        String userId = authentication.getName();
        userService.completeSurvey(userId);
        return "설문이 완료 처리되었습니다.";
    }
}
