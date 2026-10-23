package com.example.PartTrip.planner.service;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class OpenAiClientTest {

    @Test
    void 추론_모델에는_temperature_대신_reasoning_effort_를_보낸다() {
        Map<String, Object> body = OpenAiClient.requestBody("gpt-5-mini", "minimal", "sys", "user");

        assertThat(body).doesNotContainKey("temperature");
        assertThat(body).containsEntry("reasoning_effort", "minimal");
        assertThat(body).containsEntry("model", "gpt-5-mini");
        assertThat(body).containsKey("response_format");
    }

    @Test
    void 일반_모델로_되돌리면_전처럼_temperature_를_보낸다() {
        Map<String, Object> body = OpenAiClient.requestBody("gpt-4.1-mini", "minimal", "sys", "user");

        assertThat(body).containsEntry("temperature", 0.3);
        assertThat(body).doesNotContainKey("reasoning_effort");
    }

    @Test
    void 추론_모델을_이름으로_가린다() {
        assertThat(OpenAiClient.isReasoningModel("gpt-5-mini")).isTrue();
        assertThat(OpenAiClient.isReasoningModel("gpt-5.4-mini")).isTrue();
        assertThat(OpenAiClient.isReasoningModel("o4-mini")).isTrue();
        assertThat(OpenAiClient.isReasoningModel("gpt-4.1-mini")).isFalse();
        assertThat(OpenAiClient.isReasoningModel("gpt-4o-mini")).isFalse();
    }
}
