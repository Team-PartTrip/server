package com.example.PartTrip.main.service;

import com.example.PartTrip.main.enums.TourPlaceCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TourPlaceMoreCursorTest {

    private static TourPlaceImportService.Cursor cursor(int keyword, String token) {
        return new TourPlaceImportService.Cursor(keyword, token);
    }

    @Test
    @DisplayName("싸고 풀면 원래대로 돌아온다 - 토큰에 | 나 + / 가 있어도")
    void roundTrip() {
        var original = cursor(2, "Aa+b/c|d==");
        var decoded = TourPlaceImportService.Cursor.decode(original.encode());

        assertThat(decoded).isEqualTo(original);
    }

    @Test
    @DisplayName("cursor 가 없으면 첫 검색어의 첫 쪽부터")
    void emptyStartsFromBeginning() {
        assertThat(TourPlaceImportService.Cursor.decode(null)).isEqualTo(cursor(0, null));
        assertThat(TourPlaceImportService.Cursor.decode(" ")).isEqualTo(cursor(0, null));
    }

    @Test
    @DisplayName("다음 쪽 토큰이 있으면 같은 검색어의 다음 쪽")
    void nextPageOfSameKeyword() {
        assertThat(TourPlaceImportService.next(cursor(1, "t1"), "t2", 5))
                .isEqualTo(cursor(1, "t2"));
    }

    @Test
    @DisplayName("쪽이 끝나면 다음 검색어의 첫 쪽")
    void nextKeywordWhenPagesEnd() {
        assertThat(TourPlaceImportService.next(cursor(1, "t1"), null, 5))
                .isEqualTo(cursor(2, null));
    }

    @Test
    @DisplayName("마지막 검색어의 마지막 쪽이면 끝이다")
    void endsAfterLastKeyword() {
        assertThat(TourPlaceImportService.next(cursor(4, "t1"), "", 5)).isNull();
    }

    @Test
    @DisplayName("망가진 cursor 는 400 으로 막는다")
    void rejectsBrokenCursor() {
        assertThatThrownBy(() -> TourPlaceImportService.Cursor.decode("@@@"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("모든 카테고리에 검색어가 있고, 첫 검색어는 도시를 처음 열 때와 같다")
    void everyCategoryHasKeywords() {
        for (TourPlaceCategory category : TourPlaceCategory.values()) {
            assertThat(TourPlaceImportService.MORE_KEYWORDS.get(category))
                    .as(category.name())
                    .isNotEmpty();
        }
        // 첫 검색어가 같아야 이미 받아둔 10곳이 이름으로 걸러지고 그 뒤부터 이어진다
        assertThat(TourPlaceImportService.MORE_KEYWORDS.get(TourPlaceCategory.RESTAURANT).get(0))
                .isEqualTo("맛집");
    }
}
