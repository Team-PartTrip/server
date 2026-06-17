package com.example.PartTrip.controller.login;

import com.example.PartTrip.dto.login.LoginRequestDto;
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

}
