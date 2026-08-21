package com.example.PartTrip.main.search.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecentSearchRequestDto {

    // userId 는 받지 않는다. 요청이 보낸 값을 믿으면 다른 사용자의 기록을
    // 조작할 수 있으므로 인증 토큰에서 꺼내 쓴다.

    @NotNull(message = "국가를 선택해주세요.")
    private Long countryInfoId;
}
