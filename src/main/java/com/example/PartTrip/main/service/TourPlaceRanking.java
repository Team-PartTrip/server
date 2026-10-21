package com.example.PartTrip.main.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;

import java.util.Comparator;

/** Bayesian-adjusted rating used to avoid ranking a handful of perfect reviews above popular places. */
public final class TourPlaceRanking {

    public static final long REVIEW_PRIOR = 50;
    private static final double FALLBACK_AVERAGE = 4.0;

    private TourPlaceRanking() {
    }

    public static Comparator<TourPlaceEntity> byBayesianRating(Double globalAverage) {
        double average = globalAverage == null || !Double.isFinite(globalAverage)
                ? FALLBACK_AVERAGE : globalAverage;
        return Comparator.comparingDouble((TourPlaceEntity place) -> score(place, average))
                .reversed()
                .thenComparing(TourPlaceEntity::getPlaceName,
                        Comparator.nullsLast(String::compareTo))
                .thenComparing(TourPlaceEntity::getTourPlaceId,
                        Comparator.nullsLast(Long::compareTo));
    }

    public static double score(TourPlaceEntity place, double globalAverage) {
        double average = Double.isFinite(globalAverage) ? globalAverage : FALLBACK_AVERAGE;
        long reviews = place.getUserRatingCount() == null
                ? 0 : Math.max(0, place.getUserRatingCount());
        double rating = place.getRating() == null ? average : place.getRating();
        return (reviews * rating + REVIEW_PRIOR * average) / (reviews + REVIEW_PRIOR);
    }
}
