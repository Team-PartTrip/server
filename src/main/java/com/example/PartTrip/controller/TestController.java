package com.example.PartTrip.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// JWT 인증 테스트용 Controller
@RestController
public class TestController {

    // 토큰이 필요한 테스트 API
    @GetMapping("/api/test")
    public String test() {

        return "JWT 인증 성공";
    }
}