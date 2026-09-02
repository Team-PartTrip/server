package com.example.PartTrip.main.service;

import org.junit.jupiter.api.Test;

import static com.example.PartTrip.main.service.TourPlaceImportService.cleanAddress;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

// 구글 formattedAddress 는 나라마다 나라 이름·우편번호 위치가 다르다.
// 목록에서는 한 줄로 잘려서, 앞이 지저분하면 도시·구가 안 보인다.
class TourPlaceAddressTest {

    @Test
    void 일본_앞에_붙은_나라와_우편번호를_뗀다() {
        assertEquals(
                "Osaka, Chuo Ward, Namba, 1-chōme−6−８",
                cleanAddress("일본 〒542-0076 Osaka, Chuo Ward, Namba, 1-chōme−6−８"));
    }

    @Test
    void 일본_뒤에_붙은_우편번호와_나라를_뗀다() {
        assertEquals(
                "3-chōme-5-28 Minamisenba, Chuo Ward, Osaka",
                cleanAddress("3-chōme-5-28 Minamisenba, Chuo Ward, Osaka, 542-0081 일본"));
    }

    @Test
    void 베트남_뒤에_붙은_우편번호와_나라를_뗀다() {
        assertEquals(
                "323 Đ. Trần Hưng Đạo, An Hải, Đà Nẵng",
                cleanAddress("323 Đ. Trần Hưng Đạo, An Hải, Đà Nẵng 550000 베트남"));
    }

    @Test
    void 싱가포르_가운데_나라이름과_끝의_우편번호를_뗀다() {
        assertEquals("40 Carpenter St, #01-01", cleanAddress("40 Carpenter St, #01-01, 싱가포르 059919"));
    }

    // 층 번호(2층)나 건물 번호(108-1)까지 지우면 주소가 못 쓰게 된다
    @Test
    void 주소에_필요한_숫자는_남긴다() {
        assertEquals(
                "10A Siloso Bch Walk",
                cleanAddress("10A Siloso Bch Walk, 싱가포르 099008"));
    }

    @Test
    void 끝에_남는_국가코드를_뗀다() {
        assertEquals(
                "전북특별자치도 군산시 대학로 108-1 2층",
                cleanAddress("대한민국 전북특별자치도 군산시 대학로 108-1 2층 KR"));
    }

    @Test
    void 값이_없으면_null() {
        assertNull(cleanAddress(null));
        assertNull(cleanAddress("   "));
        // 나라 이름만 있던 주소는 남는 게 없다
        assertNull(cleanAddress("일본"));
    }
}
