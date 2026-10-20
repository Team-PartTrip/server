package com.example.PartTrip.profile.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.PartTrip.signup.entity.UserEntity;
import org.junit.jupiter.api.Test;

class ProfileResponseDtoTest {

    @Test
    void 카카오_계정도_저장된_이메일을_담는다() {
        UserEntity user = new UserEntity();
        user.setUserId("kakao_123");
        user.setUserMail("kim@example.com");
        user.setNickName("찬우");

        ProfileResponseDto dto = ProfileResponseDto.from(user);

        assertThat(dto.getUserId()).isEqualTo("kakao_123");
        assertThat(dto.getEmail()).isEqualTo("kim@example.com");
    }

    @Test
    void 이메일이_없으면_null_이다() {
        UserEntity user = new UserEntity();
        user.setUserId("kakao_123");
        user.setNickName("찬우");

        assertThat(ProfileResponseDto.from(user).getEmail()).isNull();
    }
}
