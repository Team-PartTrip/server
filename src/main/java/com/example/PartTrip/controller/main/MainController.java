package com.example.PartTrip.controller.main;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/api/main")
    public String mainPage() {
        return "메인 페이지 접근 성공";
    }

}
