package com.example.PartTrip.planner.dto.response;

import com.example.PartTrip.planner.dto.request.GeneratePlannerRequestDto;
import com.example.PartTrip.planner.enums.PlannerBlockType;

import java.util.List;

/** 생성 당시 설문 응답을 조회하거나 다시 생성할 때 사용하는 응답. */
public record PlannerSurveyResponseDto(Long plannerId, List<Block> blocks) {

    public record Block(
            PlannerBlockType type,
            String value,
            String placeName,
            Double latitude,
            Double longitude
    ) {
        public static Block fromRequest(GeneratePlannerRequestDto.Block block) {
            return new Block(block.getType(), block.getValue(), block.getPlaceName(),
                    block.getLatitude(), block.getLongitude());
        }

        public GeneratePlannerRequestDto.Block toRequest() {
            GeneratePlannerRequestDto.Block block = new GeneratePlannerRequestDto.Block();
            block.setType(type);
            block.setValue(value);
            block.setPlaceName(placeName);
            block.setLatitude(latitude);
            block.setLongitude(longitude);
            return block;
        }
    }

    public PlannerSurveyResponseDto {
        blocks = blocks == null ? List.of() : List.copyOf(blocks);
    }
}
