package com.example.PartTrip.profile.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileImageRuleTest {

    private static final String OLD = "/uploads/profile/3f2a.jpg";

    @Test
    void 비우기와_지금_사진_그대로와_내_폴더_사진은_받는다() {
        assertThat(ProfileService.isAllowedImage(null, OLD, "me")).isTrue();
        assertThat(ProfileService.isAllowedImage("", OLD, "me")).isTrue();
        assertThat(ProfileService.isAllowedImage(OLD, OLD, "me")).isTrue();
        assertThat(ProfileService.isAllowedImage("/uploads/profile/me/a.jpg", OLD, "me")).isTrue();
    }

    @Test
    void 남의_사진과_외부_주소와_경로_우회는_막는다() {
        assertThat(ProfileService.isAllowedImage("/uploads/profile/friend/a.jpg", OLD, "me")).isFalse();
        assertThat(ProfileService.isAllowedImage("/uploads/trip-card/1/a.jpg", OLD, "me")).isFalse();
        assertThat(ProfileService.isAllowedImage("https://example.com/a.jpg", OLD, "me")).isFalse();
        assertThat(ProfileService.isAllowedImage("/uploads/profile/me/../friend/a.jpg", OLD, "me")).isFalse();
        assertThat(ProfileService.isAllowedImage("/uploads/profile/me2/a.jpg", OLD, "me")).isFalse();
    }
}
