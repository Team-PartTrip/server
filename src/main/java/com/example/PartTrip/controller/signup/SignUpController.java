package com.example.PartTrip.controller.signup;

import com.example.PartTrip.dto.signup.SignUpRequestDto;
import com.example.PartTrip.entity.UserEntity;
import com.example.PartTrip.service.signup.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping("/signup")
    public UserEntity signup(@RequestBody SignUpRequestDto dto) {

        return signUpService.saveUser(dto);
    }
}