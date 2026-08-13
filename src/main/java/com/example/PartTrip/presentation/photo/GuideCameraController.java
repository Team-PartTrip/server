package com.example.PartTrip.presentation.photo;

import com.example.PartTrip.application.photo.data.GuideCameraImageUploadResponseDto;
import com.example.PartTrip.application.photo.data.GuideCameraRecordSaveRequestDto;
import com.example.PartTrip.application.photo.data.NearbyPlaceRecommendationResponseDto;
import com.example.PartTrip.application.photo.data.PhotoAnalysisResponseDto;
import com.example.PartTrip.application.photo.data.PhotoUploadRequestDto;
import com.example.PartTrip.application.photo.GuideCameraService;
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
import com.example.PartTrip.application.photo.data.GuideCameraMissionCreateRequestDto;
import com.example.PartTrip.application.photo.data.GuideCameraMissionResponseDto;

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

    @GetMapping("/recommendations")
    public List<NearbyPlaceRecommendationResponseDto> getRecommendations(
            @RequestParam @DecimalMin("-90.0") @DecimalMax("90.0") BigDecimal latitude,
            @RequestParam @DecimalMin("-180.0") @DecimalMax("180.0") BigDecimal longitude
    ) {
        return guideCameraService.getRecommendations(latitude, longitude);
    }

    @PostMapping("/records")
    public PhotoAnalysisResponseDto saveRecord(@Valid @RequestBody GuideCameraRecordSaveRequestDto request) {
        return guideCameraService.saveRecord(request);
    }

    @PostMapping("/missions")
    public GuideCameraMissionResponseDto createMission(
            @Valid @RequestBody GuideCameraMissionCreateRequestDto request
    ) {
        return guideCameraService.createMission(request);
    }
}
