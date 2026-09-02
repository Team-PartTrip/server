package com.example.PartTrip.worldmap.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CountryMetadataResolverTest {

    private final CountryMetadataResolver resolver = new CountryMetadataResolver();

    @Test
    void resolvesKoreanAndEnglishCountryNamesToSameCode() {
        assertThat(resolver.resolveCode("일본")).isEqualTo("JP");
        assertThat(resolver.resolveCode("Japan")).isEqualTo("JP");
    }

    @Test
    void continentCountryTotalsMatchWorldTotal() {
        assertThat(resolver.continentTotals()).hasSize(6);
        assertThat(resolver.continentTotals().values().stream().mapToInt(Integer::intValue).sum())
                .isEqualTo(CountryMetadataResolver.TOTAL_COUNTRY_COUNT);
    }
}
