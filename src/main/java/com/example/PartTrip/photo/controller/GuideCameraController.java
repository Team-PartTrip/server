package com.example.PartTrip.photo.controller;

import com.example.PartTrip.photo.dto.GuideCameraImageUploadResponseDto;
import com.example.PartTrip.photo.dto.GuideCameraRecordSaveRequestDto;
import com.example.PartTrip.photo.dto.PhotoAnalysisResponseDto;
import com.example.PartTrip.photo.dto.PhotoUploadRequestDto;
import com.example.PartTrip.photo.service.GuideCameraService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guide-camera")
public class GuideCameraController {

    private final GuideCameraService guideCameraService;

    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public GuideCameraImageUploadResponseDto uploadImage(@Valid @ModelAttribute PhotoUploadRequestDto request) {
        return guideCameraService.uploadImage(request);
    }

    @GetMapping("/results/{imageId}")
    public PhotoAnalysisResponseDto getAnalysisResult(@PathVariable Long imageId) {
        return guideCameraService.getAnalysisResult(imageId);
    }


    @PostMapping("/records")
    public PhotoAnalysisResponseDto saveRecord(@Valid @RequestBody GuideCameraRecordSaveRequestDto request) {
        return guideCameraService.saveRecord(request);
    }

}
