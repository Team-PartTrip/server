package com.example.PartTrip.controller.login;

import com.example.PartTrip.dto.login.LoginRequestDto;
import com.example.PartTrip.dto.login.LogoutRequestDto;
import com.example.PartTrip.dto.login.RefreshRequestDto;
import com.example.PartTrip.dto.login.TokenResponseDto;
import com.example.PartTrip.service.login.LoginService;
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
