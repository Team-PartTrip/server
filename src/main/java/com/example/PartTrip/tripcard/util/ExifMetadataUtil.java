package com.example.PartTrip.tripcard.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Optional;

// 사진에서 촬영 시각과 좌표를 읽는다.
//
// 시각과 좌표는 따로 논다. 위치를 끈 채 찍은 사진도 촬영 시각은 멀쩡하고,
// 타임라인은 그 시각으로 날짜를 잡는다. 그래서 좌표가 없다고 시각까지 버리지 않는다.
// 좌표가 없는 사진은 TimelineItemType.NO_INFO_PHOTO 로 표시된다.
public final class ExifMetadataUtil {

    private ExifMetadataUtil() {
    }

    public static Optional<ExifMetadata> extract(MultipartFile imageFile) {
        try (var inputStream = imageFile.getInputStream()) {
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
            ExifSubIFDDirectory exifDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            if (exifDirectory == null) {
                return Optional.empty();
            }

            // EXIF 시각에는 시간대가 없다. 카메라가 보여준 벽시계 값이 그대로 여행지의
            // 시각이라 그 값을 옮겨 적기만 한다. Date 로 한 번 바꾸면 서버 시간대를 타고,
            // 서머타임으로 없는 시각(예: 미국 3월 02:30)은 한 시간 밀린다.
            String original = exifDirectory.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            LocalDateTime takenAt = parseExifDateTime(original);
            if (takenAt == null) {
                return Optional.empty();
            }

            GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
            GeoLocation location = gpsDirectory == null ? null : gpsDirectory.getGeoLocation();
            if (location == null || location.isZero()) {
                return Optional.of(new ExifMetadata(takenAt, null, null));
            }
            return Optional.of(new ExifMetadata(takenAt, location.getLatitude(), location.getLongitude()));
        } catch (ImageProcessingException | IOException exception) {
            return Optional.empty();
        }
    }

    /** EXIF 표준 표기는 "yyyy:MM:dd HH:mm:ss" 다. 읽을 수 없으면 null */
    static LocalDateTime parseExifDateTime(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(value.trim(), EXIF_FORMAT);
        } catch (DateTimeParseException exception) {
            return null;
        }
    }

    // STRICT 가 아니면 2026:02:29 같은 없는 날짜가 02-28 로 슬쩍 바뀐다.
    // 잘못 찍힌 값이 멀쩡한 촬영 시각으로 저장되느니 못 읽은 것으로 두는 편이 낫다.
    // STRICT 에서는 연도를 yyyy(연호 기준) 대신 uuuu 로 써야 한다.
    private static final DateTimeFormatter EXIF_FORMAT =
            DateTimeFormatter.ofPattern("uuuu:MM:dd HH:mm:ss")
                    .withResolverStyle(ResolverStyle.STRICT);

    public record ExifMetadata(LocalDateTime takenAt, Double latitude, Double longitude) { }
}
