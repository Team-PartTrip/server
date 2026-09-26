package com.example.PartTrip.global.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CorsConfigTest {

    @Test
    void 배포한_웹_주소를_허용한다() {
        assertThat(CorsConfig.allowedOrigins(""))
                .contains("https://dandi-trip.vercel.app", "https://part-trip-web.vercel.app");
    }

    @Test
    void 초대_링크_웹_주소도_끝_슬래시를_떼고_허용한다() {
        assertThat(CorsConfig.allowedOrigins("https://dandi.me/"))
                .contains("https://dandi.me")
                .doesNotContain("https://dandi.me/");
        assertThat(CorsConfig.allowedOrigins("https://dandi-trip.vercel.app"))
                .filteredOn("https://dandi-trip.vercel.app"::equals).hasSize(1);
    }
}
