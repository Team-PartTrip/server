package com.example.PartTrip.application.community.data;

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
