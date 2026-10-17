package com.example.PartTrip.main.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddressLookupServiceTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void 도로명_주소를_먼저_준다() throws Exception {
        var body = mapper.readTree("""
                {"documents":[{"road_address":{"address_name":"경북 경주시 불국로 385"},
                               "address":{"address_name":"경북 경주시 진현동 15"}}]}""");

        assertThat(AddressLookupService.parse(body)).isEqualTo("경북 경주시 불국로 385");
    }

    @Test
    void 도로명_주소가_없으면_지번_주소() throws Exception {
        var body = mapper.readTree("""
                {"documents":[{"road_address":null,"address":{"address_name":"경북 경주시 진현동 15"}}]}""");

        assertThat(AddressLookupService.parse(body)).isEqualTo("경북 경주시 진현동 15");
    }

    @Test
    void 주소가_없는_곳은_null() throws Exception {
        assertThat(AddressLookupService.parse(mapper.readTree("{\"documents\":[]}"))).isNull();
    }

    @Test
    void 해외_좌표는_카카오를_부르지_않고_막는다() {
        assertThatThrownBy(() -> new AddressLookupService().lookup(35.68, 139.77))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
