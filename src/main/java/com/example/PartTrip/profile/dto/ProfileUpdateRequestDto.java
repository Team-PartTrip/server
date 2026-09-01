package com.example.PartTrip.profile.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileUpdateRequestDto {

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickName;

    private String imgUrl;
}
