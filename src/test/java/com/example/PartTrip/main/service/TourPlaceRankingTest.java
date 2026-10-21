package com.example.PartTrip.main.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TourPlaceRankingTest {

    @Test
    void 리뷰가_적은_만점_장소가_리뷰가_많은_장소보다_과대평가되지_않는다() {
        TourPlaceEntity fewReviews = place(1L, "few", 5.0, 3L);
        TourPlaceEntity manyReviews = place(2L, "many", 4.6, 1_000L);

        assertThat(List.of(fewReviews, manyReviews)
                .stream().sorted(TourPlaceRanking.byBayesianRating(4.0))
                .map(TourPlaceEntity::getTourPlaceId))
                .containsExactly(2L, 1L);
    }

    @Test
    void 리뷰수가_없는_기존_행은_리뷰수_0으로_취급하고_목록에_남긴다() {
        TourPlaceEntity noReviews = place(1L, "legacy", 5.0, null);

        assertThat(TourPlaceRanking.score(noReviews, 4.2)).isEqualTo(4.2);
        assertThat(List.of(noReviews).stream().sorted(TourPlaceRanking.byBayesianRating(4.2)))
                .containsExactly(noReviews);
    }

    private static TourPlaceEntity place(Long id, String name, Double rating, Long reviews) {
        TourPlaceEntity place = new TourPlaceEntity();
        place.setTourPlaceId(id);
        place.setPlaceName(name);
        place.setRating(rating);
        place.setUserRatingCount(reviews);
        return place;
    }
}
