package com.example.PartTrip.dto.community;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardRequestDto {
    private String title;
    private String content;
    private List<String> images;
}
