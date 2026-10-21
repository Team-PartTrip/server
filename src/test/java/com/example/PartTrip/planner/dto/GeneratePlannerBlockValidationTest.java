package com.example.PartTrip.planner.dto;

import com.example.PartTrip.planner.dto.request.GeneratePlannerRequestDto;
import com.example.PartTrip.planner.enums.PlannerBlockType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GeneratePlannerBlockValidationTest {

    @Test
    void 열거형_블록은_정의된_선택지만_허용한다() {
        GeneratePlannerRequestDto request = request(block(PlannerBlockType.TRAVEL_TYPE, "미식"));
        assertThat(request.isBlockSelectionValid()).isTrue();

        request.setBlocks(List.of(block(PlannerBlockType.TRAVEL_TYPE, "임의 문자열")));
        assertThat(request.isBlockSelectionValid()).isFalse();
    }

    @Test
    void 자유입력_블록은_사용자_입력을_허용한다() {
        assertThat(request(
                block(PlannerBlockType.MUST_INCLUDE, "경포대"),
                block(PlannerBlockType.EXCLUDE, "산악 코스"),
                block(PlannerBlockType.DEPARTURE_PLACE, "강릉역"))
                .isBlockSelectionValid()).isTrue();
    }

    @Test
    void 단일선택_블록에_값이_둘_이상이면_거부한다() {
        assertThat(request(
                block(PlannerBlockType.WALKING, "10분 이내"),
                block(PlannerBlockType.WALKING, "30분 이내"))
                .isBlockSelectionValid()).isFalse();
    }

    @Test
    void 일정밀도는_선택지_또는_1부터_6까지의_숫자만_허용한다() {
        assertThat(request(block(PlannerBlockType.DAILY_DENSITY, "하루 1~2곳"))
                .isBlockSelectionValid()).isTrue();
        assertThat(request(block(PlannerBlockType.DAILY_DENSITY, "직접 지정"))
                .isBlockSelectionValid()).isTrue();
        assertThat(request(block(PlannerBlockType.DAILY_DENSITY, "6"))
                .isBlockSelectionValid()).isTrue();
        assertThat(request(block(PlannerBlockType.DAILY_DENSITY, "7"))
                .isBlockSelectionValid()).isFalse();
    }

    private static GeneratePlannerRequestDto request(GeneratePlannerRequestDto.Block... blocks) {
        GeneratePlannerRequestDto request = new GeneratePlannerRequestDto();
        request.setBlocks(List.of(blocks));
        return request;
    }

    private static GeneratePlannerRequestDto.Block block(PlannerBlockType type, String value) {
        GeneratePlannerRequestDto.Block block = new GeneratePlannerRequestDto.Block();
        block.setType(type);
        block.setValue(value);
        return block;
    }
}
