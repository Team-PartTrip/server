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

public final class ExifMetadataUtil {

    private ExifMetadataUtil() {
    }

    public static Optional<ExifMetadata> extract(MultipartFile imageFile) {
        try (var inputStream = imageFile.getInputStream()) {
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
            ExifSubIFDDirectory exifDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
            if (exifDirectory == null || gpsDirectory == null) {
                return Optional.empty();
            }

            Date originalDate = exifDirectory.getDateOriginal();
            GeoLocation location = gpsDirectory.getGeoLocation();
            if (originalDate == null || location == null || location.isZero()) {
                return Optional.empty();
            }
            LocalDateTime takenAt = LocalDateTime.ofInstant(originalDate.toInstant(), ZoneId.systemDefault());
            return Optional.of(new ExifMetadata(takenAt, location.getLatitude(), location.getLongitude()));
        } catch (ImageProcessingException | IOException exception) {
            return Optional.empty();
        }
    }

    public record ExifMetadata(LocalDateTime takenAt, Double latitude, Double longitude) { }
}
