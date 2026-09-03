package com.example.PartTrip.worldmap.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AcquireCountryResponseDtoSerializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void isNew_이름으로_직렬화한다() throws Exception {
        AcquireCountryResponseDto response = AcquireCountryResponseDto.builder()
                .countryCode("JP")
                .isNew(true)
                .build();

        JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(response));

        assertThat(json.path("isNew").asBoolean()).isTrue();
        assertThat(json.has("new")).isFalse();
    }
}
