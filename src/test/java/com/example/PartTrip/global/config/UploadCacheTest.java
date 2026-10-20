package com.example.PartTrip.global.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UploadCacheTest {

    @Test
    void 업로드_사진은_공개_캐시로_1년_둔다() {
        assertThat(WebConfig.UPLOAD_CACHE.getHeaderValue())
                .contains("max-age=31536000")
                .contains("public")
                .contains("immutable")
                .doesNotContain("no-store");
    }
}
