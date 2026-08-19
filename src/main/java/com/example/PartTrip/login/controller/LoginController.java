package com.example.PartTrip.login.controller;

import com.example.PartTrip.login.dto.LoginRequestDto;
import com.example.PartTrip.login.dto.LogoutRequestDto;
import com.example.PartTrip.login.dto.RefreshRequestDto;
import com.example.PartTrip.login.dto.TokenResponseDto;
import com.example.PartTrip.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody LoginRequestDto dto) {
        return loginService.login(dto);
    }

    @PostMapping("/refresh")
    public TokenResponseDto refresh(@RequestBody RefreshRequestDto dto) {
        return loginService.refresh(dto);
    }

    @PostMapping("/logout")
    public String logout(@RequestBody LogoutRequestDto dto) {
        return loginService.logout(dto);
    }

}
