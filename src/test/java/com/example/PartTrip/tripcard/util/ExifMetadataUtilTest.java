package com.example.PartTrip.tripcard.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;

import java.time.LocalDateTime;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

// 촬영 시각은 서버가 어느 시간대에 있든 같아야 한다.
// 타임라인의 날짜와 정렬이 이 값에서 나오기 때문이다.
class ExifMetadataUtilTest {

    @Test
    @ResourceLock(Resources.TIME_ZONE)
    void 서버_시간대가_달라도_촬영_시각은_같다() {
        TimeZone original = TimeZone.getDefault();
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
            LocalDateTime utc = ExifMetadataUtil.parseExifDateTime("2026:08:15 14:30:00");

            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
            LocalDateTime seoul = ExifMetadataUtil.parseExifDateTime("2026:08:15 14:30:00");

            assertEquals(LocalDateTime.of(2026, 8, 15, 14, 30), utc);
            assertEquals(utc, seoul);
        } finally {
            TimeZone.setDefault(original);
        }
    }

    @Test
    @ResourceLock(Resources.TIME_ZONE)
    void 서머타임으로_없는_시각도_적힌_그대로_읽는다() {
        TimeZone original = TimeZone.getDefault();
        try {
            // 미국 동부는 이 날 02:00 에서 03:00 으로 건너뛴다. 02:30 은 존재하지 않는다.
            TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
            assertEquals(LocalDateTime.of(2026, 3, 8, 2, 30),
                    ExifMetadataUtil.parseExifDateTime("2026:03:08 02:30:00"));
        } finally {
            TimeZone.setDefault(original);
        }
    }

    @Test
    void 읽을_수_없으면_널이다() {
        assertNull(ExifMetadataUtil.parseExifDateTime(null));
        assertNull(ExifMetadataUtil.parseExifDateTime("   "));
        assertNull(ExifMetadataUtil.parseExifDateTime("2026-08-15 14:30:00"));
    }

    @Test
    void 없는_날짜는_보정하지_않고_거부한다() {
        // 2026 년은 윤년이 아니다. 느슨하게 읽으면 02-28 로 바뀐다.
        assertNull(ExifMetadataUtil.parseExifDateTime("2026:02:29 14:30:00"));
        assertNull(ExifMetadataUtil.parseExifDateTime("2026:13:01 00:00:00"));
    }
}
