package com.example.PartTrip.tripcard.service.impl;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

// 코멘트 규칙은 사진 추가와 코멘트 수정이 함께 쓴다.
// 한쪽만 고쳐서 규칙이 갈라지는 일이 없도록 여기서 잡는다.
class TripCardCommentRuleTest {

    private String normalize(String comment) throws Exception {
        Method method = TripCardEntryServiceImpl.class
                .getDeclaredMethod("normalizeComment", String.class);
        method.setAccessible(true);
        try {
            return (String) method.invoke(
                    new TripCardEntryServiceImpl(null, null, null, null), comment);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test
    void 앞뒤_공백은_지운다() throws Exception {
        assertEquals("날씨 좋았음", normalize("  날씨 좋았음  "));
    }

    @Test
    void 공백만_있으면_코멘트가_없는_것이다() throws Exception {
        assertNull(normalize(null));
        assertNull(normalize(""));
        assertNull(normalize("   "));
        // 전각 공백. trim() 은 이걸 남긴다.
        assertNull(normalize("　　"));
    }

    @Test
    void 백자를_넘으면_거부한다() throws Exception {
        assertEquals(100, normalize("가".repeat(100)).length());
        assertThrows(IllegalArgumentException.class, () -> normalize("가".repeat(101)));
    }
}
