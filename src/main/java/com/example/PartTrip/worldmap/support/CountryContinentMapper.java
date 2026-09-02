package com.example.PartTrip.worldmap.support;

import com.example.PartTrip.worldmap.enums.Continent;

import java.util.Locale;
import java.util.Set;

public final class CountryContinentMapper {

    private static final Set<String> AFRICA = codes("DZ AO BJ BW BF BI CV CM CF TD KM CG CD CI DJ EG GQ ER SZ ET GA GM GH GN GW KE LS LR LY MG MW ML MR MU MA MZ NA NE NG RW ST SN SC SL SO ZA SS SD TZ TG TN UG EH ZM ZW SH RE YT");
    private static final Set<String> ASIA = codes("AF AM AZ BH BD BT BN KH CN CY GE IN ID IR IQ IL JP JO KZ KW KG LA LB MY MV MN MM NP KP OM PK PS PH QA SA SG KR LK SY TW TJ TH TL TR TM AE UZ VN YE HK MO");
    private static final Set<String> EUROPE = codes("AL AD AT BY BE BA BG HR CZ DK EE FI FR DE GR VA HU IS IE IT LV LI LT LU MT MD MC ME NL MK NO PL PT RO RU SM RS SK SI ES SE CH UA GB AX FO GG GI IM JE SJ XK");
    private static final Set<String> NORTH_AMERICA = codes("AG BS BB BZ CA CR CU DM DO SV GD GT HT HN JM MX NI PA KN LC VC US AI AW BM BQ VG KY CW GL GP MQ MS PR BL MF PM SX TC VI");
    private static final Set<String> SOUTH_AMERICA = codes("AR BO BR CL CO EC GY PY PE SR UY VE FK GF GS");
    private static final Set<String> OCEANIA = codes("AU FJ KI MH FM NR NZ PW PG WS SB TO TV VU AS CK PF GU NC NU NF MP PN TK UM WF CC CX HM");
    private static final Set<String> ANTARCTICA = codes("AQ BV TF");

    private CountryContinentMapper() {
    }

    public static Continent getContinent(String countryCode) {
        String code = countryCode == null ? "" : countryCode.toUpperCase(Locale.ROOT);
        if (AFRICA.contains(code)) return Continent.AFRICA;
        if (ASIA.contains(code)) return Continent.ASIA;
        if (EUROPE.contains(code)) return Continent.EUROPE;
        if (NORTH_AMERICA.contains(code)) return Continent.NORTH_AMERICA;
        if (SOUTH_AMERICA.contains(code)) return Continent.SOUTH_AMERICA;
        if (OCEANIA.contains(code)) return Continent.OCEANIA;
        if (ANTARCTICA.contains(code)) return Continent.ANTARCTICA;
        return Continent.OTHER;
    }

    private static Set<String> codes(String codes) {
        return Set.of(codes.split(" "));
    }
}
