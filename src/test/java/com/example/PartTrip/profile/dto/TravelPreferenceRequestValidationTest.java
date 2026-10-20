package com.example.PartTrip.profile.dto;

import com.example.PartTrip.profile.enums.PreferredTransport;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TravelPreferenceRequestValidationTest {

    private final Validator validator = Validation
            .buildDefaultValidatorFactory()
            .getValidator();

    /** 하루 일정 개수의 허용 범위 양 끝은 정상 요청이다. */
    @Test
    void 하루_일정_개수는_1개부터_10개까지_허용한다() {
        assertThat(validate(request(1))).isEmpty();
        assertThat(validate(request(10))).isEmpty();
    }

    /** 하루 일정 개수가 허용 범위를 벗어나면 요청을 거부한다. */
    @Test
    void 하루_일정_개수가_범위를_벗어나면_거부한다() {
        assertThat(validate(request(0)))
                .extracting(ConstraintViolation::getMessage)
                .contains("하루 일정 개수는 1개 이상이어야 합니다.");
        assertThat(validate(request(11)))
                .extracting(ConstraintViolation::getMessage)
                .contains("하루 일정 개수는 10개 이하여야 합니다.");
    }

    /** 이동수단과 계단 여부는 반드시 명시해야 한다. */
    @Test
    void 필수_설정이_없으면_거부한다() {
        TravelPreferenceRequestDto request = request(3);
        request.setPreferredTransport(null);
        request.setCanUseStairs(null);

        assertThat(validate(request))
                .extracting(ConstraintViolation::getMessage)
                .contains(
                        "선호 이동수단은 필수입니다.",
                        "계단 이용 가능 여부는 필수입니다.");
    }

    /** 유효성 검사에 사용할 정상 요청을 만든다. */
    private TravelPreferenceRequestDto request(int dailyScheduleCount) {
        TravelPreferenceRequestDto request = new TravelPreferenceRequestDto();
        request.setPreferredTransport(PreferredTransport.PUBLIC_TRANSIT);
        request.setDailyScheduleCount(dailyScheduleCount);
        request.setCanUseStairs(true);
        return request;
    }

    /** 요청 DTO의 Bean Validation 결과를 반환한다. */
    private Set<ConstraintViolation<TravelPreferenceRequestDto>> validate(
            TravelPreferenceRequestDto request
    ) {
        return validator.validate(request);
    }
}
