package com.example.PartTrip.main.service;

import com.example.PartTrip.main.entity.FestivalEntity;
import com.example.PartTrip.main.repository.FestivalRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KoreaFestivalImportService {

    static final String KOREA = "대한민국";
    private static final String URL = "https://apis.data.go.kr/B551011/KorService2/searchFestival2";
    private static final int PAGE_SIZE = 100;
    private static final int MAX_PAGES = 20;
    static final int MAX_DAYS = 60;
    private static final DateTimeFormatter TOUR_DATE = DateTimeFormatter.BASIC_ISO_DATE;

    private final FestivalRepository festivalRepository;
    private final ObjectMapper objectMapper;

    // 공공데이터포털 "한국관광공사_국문 관광정보 서비스_GW" 의 Decoding 인증키. 비어 있으면 받지 않는다
    @Value("${tour-api.service-key:}")
    private String serviceKey;

    private final RestClient restClient = RestClient.builder()
            .requestFactory(ClientHttpRequestFactoryBuilder.detect().build(
                    ClientHttpRequestFactorySettings.defaults()
                            .withConnectTimeout(Duration.ofSeconds(5))
                            .withReadTimeout(Duration.ofSeconds(20))))
            .build();

    @Scheduled(cron = "0 30 4 * * *")
    public void importDaily() {
        importFrom(LocalDate.now().minusMonths(1).withDayOfMonth(1));
    }

    public int importFrom(LocalDate from) {
        if (serviceKey == null || serviceKey.isBlank()) {
            log.info("tour-api.service-key 가 없어 국내 축제를 받지 않습니다");
            return 0;
        }
        List<FestivalEntity> fetched = new ArrayList<>();
        for (int page = 1; page <= MAX_PAGES; page++) {
            JsonNode body;
            try {
                body = objectMapper.readTree(request(from, page));
            } catch (Exception e) {
                // 키가 틀리면 JSON 이 아니라 XML 오류가 온다
                log.warn("국내 축제 받기 실패 ({}쪽): {}", page, e.getMessage());
                break;
            }
            fetched.addAll(parse(body));
            int total = body.path("response").path("body").path("totalCount").asInt(0);
            // 걸러내기 전 개수로 본다. 긴 행사를 뺀 개수로 보면 첫 쪽에서 멈춘다 (#177)
            if (itemsOf(body).size() < PAGE_SIZE || page * PAGE_SIZE >= total) {
                break;
            }
        }
        int saved = upsert(fetched);
        log.info("국내 축제 {}건 저장 ({} 이후)", saved, from);
        return saved;
    }

    private String request(LocalDate from, int page) {
        String url = URL
                // 인증키에 + / = 가 들어 있어 인코딩해 붙인다
                + "?serviceKey=" + URLEncoder.encode(serviceKey, StandardCharsets.UTF_8)
                + "&MobileOS=ETC&MobileApp=PartTrip&_type=json&arrange=A"
                + "&numOfRows=" + PAGE_SIZE + "&pageNo=" + page
                + "&eventStartDate=" + from.format(TOUR_DATE);
        return restClient.get().uri(URI.create(url)).retrieve().body(String.class);
    }

    int upsert(List<FestivalEntity> fetched) {
        if (fetched.isEmpty()) {
            return 0;
        }
        Map<String, FestivalEntity> existing = festivalRepository
                .findBySourceIdIn(fetched.stream().map(FestivalEntity::getSourceId).toList())
                .stream()
                .collect(Collectors.toMap(FestivalEntity::getSourceId, Function.identity()));
        List<FestivalEntity> rows = fetched.stream()
                .map(fresh -> {
                    FestivalEntity row = existing.get(fresh.getSourceId());
                    if (row == null) {
                        return fresh;
                    }
                    row.setTitle(fresh.getTitle());
                    row.setCategory(fresh.getCategory());
                    row.setDescription(fresh.getDescription());
                    row.setStartDate(fresh.getStartDate());
                    row.setEndDate(fresh.getEndDate());
                    row.setLocation(fresh.getLocation());
                    row.setImageUrl(fresh.getImageUrl());
                    return row;
                })
                .toList();
        return festivalRepository.saveAll(rows).size();
    }

    static List<FestivalEntity> parse(JsonNode body) {
        return itemsOf(body).stream()
                .map(KoreaFestivalImportService::toEntity)
                .filter(festival -> festival != null)
                .toList();
    }

    static List<JsonNode> itemsOf(JsonNode body) {
        JsonNode item = body.path("response").path("body").path("items").path("item");
        List<JsonNode> nodes = new ArrayList<>();
        if (item.isArray()) {
            item.forEach(nodes::add);
        } else if (item.isObject()) {
            nodes.add(item);
        }
        return nodes;
    }

    static FestivalEntity toEntity(JsonNode node) {
        String sourceId = text(node, "contentid");
        String title = text(node, "title");
        String start = date(text(node, "eventstartdate"));
        if (sourceId == null || title == null || start == null) {
            return null;
        }
        String end = date(text(node, "eventenddate"));
        if (end != null && ChronoUnit.DAYS.between(LocalDate.parse(start), LocalDate.parse(end)) + 1 > MAX_DAYS) {
            return null;
        }
        FestivalEntity festival = new FestivalEntity();
        festival.setSourceId(sourceId);
        festival.setCountryName(KOREA);
        festival.setTitle(title);
        festival.setCategory(categoryOf(text(node, "lclsSystm2"), text(node, "cat2")));
        festival.setStartDate(start);
        festival.setEndDate(end);
        String address = text(node, "addr1");
        festival.setLocation(address == null ? "장소 정보 없음" : address);
        // 목록 API 는 소개글을 주지 않는다. 상세 화면에 기간이라도 보이게 한다
        festival.setDescription("기간 " + dotted(start) + (end == null || end.equals(start) ? "" : " ~ " + dotted(end)));
        festival.setImageUrl(https(text(node, "firstimage")));
        return festival;
    }

    static String categoryOf(String lclsSystm2, String cat2) {
        if ("EV01".equals(lclsSystm2) || (lclsSystm2 == null && "A0207".equals(cat2))) {
            return "축제";
        }
        if ("EV02".equals(lclsSystm2) || (lclsSystm2 == null && "A0208".equals(cat2))) {
            return "공연";
        }
        return "행사";
    }

    static String date(String raw) {
        if (raw == null || !raw.matches("\\d{8}")) {
            return null;
        }
        return raw.substring(0, 4) + "-" + raw.substring(4, 6) + "-" + raw.substring(6);
    }

    private static String dotted(String isoDate) {
        return isoDate.replace('-', '.');
    }

    static String https(String url) {
        if (url == null) {
            return null;
        }
        return url.startsWith("http://") ? "https://" + url.substring("http://".length()) : url;
    }

    private static String text(JsonNode node, String field) {
        String value = node.path(field).asText(null);
        return value == null || value.isBlank() ? null : value.trim();
    }
}
