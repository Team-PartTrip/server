package com.example.PartTrip.global.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageStorageServiceTest {

    @Test
    void 저장한_파일을_공개_URL_로_지운다(@TempDir Path root) throws IOException {
        ImageStorageService storage = new ImageStorageService(root.toString(), "/uploads");
        Path file = root.resolve("trip-card/7/photo.jpg");
        Files.createDirectories(file.getParent());
        Files.writeString(file, "x");

        assertTrue(storage.delete("/uploads/trip-card/7/photo.jpg"));
        assertFalse(Files.exists(file));
    }

    @Test
    void 저장소_밖은_지우지_않는다(@TempDir Path root) throws IOException {
        ImageStorageService storage = new ImageStorageService(root.resolve("inside").toString(), "/uploads");
        Files.createDirectories(root.resolve("inside"));
        Path outside = root.resolve("secret.txt");
        Files.writeString(outside, "x");

        assertFalse(storage.delete("/uploads/../secret.txt"));
        assertTrue(Files.exists(outside));
    }

    @Test
    void 없는_파일이나_남의_경로는_조용히_넘어간다(@TempDir Path root) {
        ImageStorageService storage = new ImageStorageService(root.toString(), "/uploads");

        assertFalse(storage.delete(null));
        assertFalse(storage.delete("https://example.com/photo.jpg"));
        assertFalse(storage.delete("/uploads/trip-card/7/없는파일.jpg"));
    }
}
