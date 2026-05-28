package com.example.PartTrip.controller.signup;

import com.example.PartTrip.dto.signup.SignUpRequestDto;
import com.example.PartTrip.entity.signup.UserEntity;
import com.example.PartTrip.service.signup.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
