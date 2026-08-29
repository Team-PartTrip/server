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
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

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

            // EXIF 시각에는 시간대가 없다. 인자를 안 주면 GMT 로 읽고 서버 시간대로 옮겨
            // 서버가 어디에 있느냐에 따라 날짜가 달라진다. 카메라가 찍은 벽시계 값을
            // 그대로 쓰기 위해 같은 시간대를 넣고 같은 시간대로 되돌린다.
            Date originalDate = exifDirectory.getDateOriginal(TimeZone.getDefault());
            if (originalDate == null) {
                return Optional.empty();
            }
            LocalDateTime takenAt = LocalDateTime.ofInstant(originalDate.toInstant(), ZoneId.systemDefault());

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

    public record ExifMetadata(LocalDateTime takenAt, Double latitude, Double longitude) { }
}
