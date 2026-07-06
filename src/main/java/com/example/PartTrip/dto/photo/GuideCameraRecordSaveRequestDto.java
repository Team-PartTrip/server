package com.example.PartTrip.dto.photo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class GuideCameraRecordSaveRequestDto {
    @NotNull(message = "사진 ID는 필수입니다.")
    private Long photoId;

    @Size(max = 100, message = "코멘트 제목은 100자 이하로 입력해주세요.")
    private String commTitle;

    @Size(max = 1000, message = "코멘트 내용은 1000자 이하로 입력해주세요.")
    private String commContent;

    private LocalDate photoDate;
}
