package com.example.PartTrip.controller;

import com.example.PartTrip.dto.UserManageDto;
import com.example.PartTrip.entity.UserEntity;
import com.example.PartTrip.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserManageController {

    private final UserService userService;

    @PostMapping("/signup")
    public UserEntity signup(@RequestBody UserManageDto dto) {

        return userService.saveUser(dto);
    }

}
