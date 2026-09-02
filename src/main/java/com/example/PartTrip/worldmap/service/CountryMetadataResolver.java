package com.example.PartTrip.worldmap.service;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Component
public class CountryMetadataResolver {

    public static final int TOTAL_COUNTRY_COUNT = 195;

    private static final Map<String, Set<String>> CONTINENT_CODES = createContinentCodes();

    public String resolveCode(String countryName) {
        String normalized = normalize(countryName);
        if (normalized.matches("[a-z]{2}")) {
            return normalized.toUpperCase(Locale.ROOT);
        }

        return Arrays.stream(Locale.getISOCountries())
                .filter(code -> {
                    Locale locale = Locale.of("", code);
                    return normalize(locale.getDisplayCountry(Locale.KOREAN)).equals(normalized)
                            || normalize(locale.getDisplayCountry(Locale.ENGLISH)).equals(normalized);
                })
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "국가 코드를 확인할 수 없습니다: " + countryName));
    }

    public String resolveCountryName(String countryCode) {
        if (countryCode == null || countryCode.length() != 2) {
            throw new IllegalArgumentException("올바른 국가 코드를 입력해주세요.");
        }
        return Locale.of("", countryCode.toUpperCase(Locale.ROOT))
                .getDisplayCountry(Locale.KOREAN);
    }

    public String resolveContinent(String countryCode) {
        String normalizedCode = countryCode.toUpperCase(Locale.ROOT);
        return CONTINENT_CODES.entrySet().stream()
                .filter(entry -> entry.getValue().contains(normalizedCode))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("기타");
    }

    public Map<String, Integer> continentTotals() {
        Map<String, Integer> totals = new LinkedHashMap<>();
        CONTINENT_CODES.forEach((continent, codes) -> totals.put(continent, codes.size()));
        return totals;
    }

    private static Map<String, Set<String>> createContinentCodes() {
        Map<String, Set<String>> result = new LinkedHashMap<>();
        result.put("아프리카", codes("DZ AO BJ BW BF BI CM CV CF TD KM CD CG CI DJ EG GQ ER SZ ET GA GM GH GN GW KE LS LR LY MG MW ML MR MU MA MZ NA NE NG RW ST SN SC SL SO ZA SS SD TZ TG TN UG ZM ZW"));
        result.put("아시아", codes("AF AM AZ BH BD BT BN KH CN CY GE IN ID IR IQ IL JP JO KZ KW KG LA LB MY MV MN MM NP KP OM PK PS PH QA SA SG LK SY TW TJ TH TL TR TM AE UZ VN YE"));
        result.put("유럽", codes("AL AD AT BY BE BA BG HR CZ DK EE FI FR DE GR VA HU IS IE IT LV LI LT LU MT MD MC ME NL MK NO PL PT RO RU SM RS SK SI ES SE CH UA GB"));
        result.put("북아메리카", codes("AG BS BB BZ CA CR CU DM DO SV GD GT HT HN JM MX NI PA KN LC VC TT US"));
        result.put("남아메리카", codes("AR BO BR CL CO EC GY PY PE SR UY VE"));
        result.put("오세아니아", codes("AU FJ KI MH FM NR NZ PW PG WS SB TO TV VU"));
        return result;
    }

    private static Set<String> codes(String value) {
        return Set.of(value.split(" "));
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
