package com.example.PartTrip.service.photo;

import com.example.PartTrip.entity.photo.PhotoAnalysisEntity;
import com.example.PartTrip.entity.photo.PhotoEntity;
import com.example.PartTrip.enums.photo.PhotoAnalysisAccuracyCategory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class OpenAiWikipediaPhotoAnalyzer implements PhotoAnalyzer {

    private static final double MIN_CONFIDENCE = 0.55;
    private static final int MAX_WIKIPEDIA_EXTRACT_LENGTH = 3500;
    private static final String USER_AGENT = "PartTrip/1.0";

    private final MockPhotoAnalyzer mockPhotoAnalyzer;
    private final ObjectMapper objectMapper;
    private final RestClient restClient = RestClient.builder().build();

    @Value("${google.vision.api-key:${GOOGLE_VISION_API_KEY:}}")
    private String googleVisionApiKey;

    @Value("${google.places.api-key:${GOOGLE_PLACES_API_KEY:}}")
    private String googlePlacesApiKey;

    @Value("${openai.api-key:${OPENAI_API_KEY:}}")
    private String openAiApiKey;

    @Value("${openai.model:gpt-4o-mini}")
    private String openAiModel;

    @Override
    public PhotoAnalysisEntity analyze(PhotoEntity photo, MultipartFile imageFile) {
        if (!StringUtils.hasText(googleVisionApiKey)
                || !StringUtils.hasText(googlePlacesApiKey)
                || !StringUtils.hasText(openAiApiKey)) {
            return mockPhotoAnalyzer.analyze(photo, imageFile);
        }

        try {
            VisionCandidate candidate = recognizeImage(imageFile);

            log.info("[Vision 최종 선택] description='{}', score={}, source={}",
                    candidate.description(),
                    candidate.score(),
                    candidate.source());

            if (candidate.score() < MIN_CONFIDENCE) {
                return failedAnalysis(photo, "사진 속 대상을 정확하게 특정하지 못했습니다.");
            }

            GooglePlaceInfo placeInfo = findGooglePlace(candidate.description(), photo).orElse(null);

            String searchKeyword = placeInfo != null && StringUtils.hasText(placeInfo.displayName())
                    ? placeInfo.displayName()
                    : candidate.description();

            WikipediaArticle article = findWikipediaArticle(searchKeyword)
                    .orElseGet(() -> findWikipediaArticle(searchKeyword + " 문화재").orElse(null));

            if (article == null) {
                return failedAnalysis(photo, "사진 속 대상과 일치하는 위키백과 정보를 찾지 못했습니다.");
            }

            JsonNode formatted = formatWithOpenAi(candidate, placeInfo, article);

            return PhotoAnalysisEntity.builder()
                    .photo(photo)
                    .title(text(formatted, "title", article.title()))
                    .era(text(formatted, "era", "확인 필요"))
                    .designation(text(formatted, "designation", "확인 필요"))
                    .overview(text(formatted, "overview", article.extract()))
                    .background(text(formatted, "background", "확인 필요"))
                    .features(text(formatted, "features", "확인 필요"))
                    .currentStatus(text(formatted, "currentStatus", "확인 필요"))
                    .sourceName(placeInfo != null ? "Google Places, Wikipedia" : "Wikipedia")
                    .sourceUrl(placeInfo != null && StringUtils.hasText(placeInfo.googleMapsUri())
                            ? placeInfo.googleMapsUri()
                            : article.url())
                    .photoAnalysisAccuracyCategory(toAccuracyCategory(candidate.score()))
                    .build();

        } catch (RuntimeException exception) {
            log.error("[사진 분석 실패]", exception);
            return failedAnalysis(photo, "사진 분석 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }
    }

    private VisionCandidate recognizeImage(MultipartFile imageFile) {
        String imageContent = toBase64(imageFile);

        Map<String, Object> requestBody = Map.of(
                "requests", List.of(Map.of(
                        "image", Map.of("content", imageContent),
                        "features", List.of(
                                Map.of("type", "LANDMARK_DETECTION", "maxResults", 5),
                                Map.of("type", "WEB_DETECTION", "maxResults", 5),
                                Map.of("type", "LABEL_DETECTION", "maxResults", 5)
                        )
                ))
        );

        JsonNode response = restClient.post()
                .uri("https://vision.googleapis.com/v1/images:annotate?key={apiKey}", googleVisionApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(JsonNode.class);

        JsonNode firstResponse = response == null
                ? objectMapper.createObjectNode()
                : response.path("responses").path(0);

        if (!firstResponse.path("error").isMissingNode()) {
            throw new IllegalArgumentException("Google Vision API 분석에 실패했습니다.");
        }

        List<VisionCandidate> landmarkCandidates = new ArrayList<>();
        List<VisionCandidate> webCandidates = new ArrayList<>();
        List<VisionCandidate> bestGuessCandidates = new ArrayList<>();
        List<VisionCandidate> labelCandidates = new ArrayList<>();

        addCandidates(landmarkCandidates, firstResponse.path("landmarkAnnotations"), "description");
        addCandidates(webCandidates, firstResponse.path("webDetection").path("webEntities"), "description");
        addBestGuessCandidates(bestGuessCandidates, firstResponse.path("webDetection").path("bestGuessLabels"));
        addCandidates(labelCandidates, firstResponse.path("labelAnnotations"), "description");

        log.info("[Vision Landmark 후보] {}", landmarkCandidates);
        log.info("[Vision Web 후보] {}", webCandidates);
        log.info("[Vision BestGuess 후보] {}", bestGuessCandidates);
        log.info("[Vision Label 후보] {}", labelCandidates);

        return findBestCandidate(landmarkCandidates, "LANDMARK")
                .or(() -> findBestCandidate(webCandidates, "WEB"))
                .or(() -> findBestCandidate(bestGuessCandidates, "BEST_GUESS"))
                .or(() -> findBestCandidate(labelCandidates, "LABEL"))
                .orElse(new VisionCandidate("", 0.0, "NONE"));
    }

    private Optional<VisionCandidate> findBestCandidate(List<VisionCandidate> candidates, String source) {
        return candidates.stream()
                .filter(candidate -> StringUtils.hasText(candidate.description()))
                .max(Comparator.comparingDouble(VisionCandidate::score))
                .map(candidate -> new VisionCandidate(
                        candidate.description(),
                        candidate.score(),
                        source
                ));
    }

    private void addCandidates(List<VisionCandidate> candidates, JsonNode nodes, String descriptionField) {
        if (!nodes.isArray()) {
            return;
        }

        nodes.forEach(node -> candidates.add(new VisionCandidate(
                node.path(descriptionField).asText(""),
                node.path("score").asDouble(0.0),
                "UNKNOWN"
        )));
    }

    private void addBestGuessCandidates(List<VisionCandidate> candidates, JsonNode nodes) {
        if (!nodes.isArray()) {
            return;
        }

        nodes.forEach(node -> candidates.add(new VisionCandidate(
                node.path("label").asText(""),
                0.65,
                "BEST_GUESS"
        )));
    }

    private Optional<GooglePlaceInfo> findGooglePlace(String query, PhotoEntity photo) {
        if (!StringUtils.hasText(query)) {
            return Optional.empty();
        }

        Map<String, Object> requestBody = Map.of(
                "textQuery", query,
                "languageCode", "ko",
                "locationBias", Map.of(
                        "circle", Map.of(
                                "center", Map.of(
                                        "latitude", photo.getLatitude().doubleValue(),
                                        "longitude", photo.getLongitude().doubleValue()
                                ),
                                "radius", 5000.0
                        )
                )
        );

        try {
            JsonNode response = restClient.post()
                    .uri("https://places.googleapis.com/v1/places:searchText")
                    .header("X-Goog-Api-Key", googlePlacesApiKey)
                    .header("X-Goog-FieldMask",
                            "places.id,places.displayName,places.formattedAddress,places.googleMapsUri,places.location,places.rating,places.userRatingCount,places.primaryTypeDisplayName")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(JsonNode.class);

            JsonNode place = response == null
                    ? objectMapper.createObjectNode()
                    : response.path("places").path(0);

            if (!StringUtils.hasText(place.path("id").asText(""))) {
                return Optional.empty();
            }

            return Optional.of(new GooglePlaceInfo(
                    place.path("id").asText(""),
                    place.path("displayName").path("text").asText(""),
                    place.path("formattedAddress").asText(""),
                    place.path("googleMapsUri").asText(""),
                    place.path("rating").asDouble(0.0),
                    place.path("userRatingCount").asInt(0),
                    place.path("primaryTypeDisplayName").path("text").asText("")
            ));

        } catch (RuntimeException exception) {
            log.info("[Google Places 조회 실패] query={}", query, exception);
            return Optional.empty();
        }
    }

    private Optional<WikipediaArticle> findWikipediaArticle(String query) {
        return searchWikipedia("ko", query).or(() -> searchWikipedia("en", query));
    }

    private Optional<WikipediaArticle> searchWikipedia(String language, String query) {
        JsonNode searchResponse = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(language + ".wikipedia.org")
                        .path("/w/api.php")
                        .queryParam("action", "query")
                        .queryParam("list", "search")
                        .queryParam("srsearch", query)
                        .queryParam("srlimit", 1)
                        .queryParam("format", "json")
                        .queryParam("utf8", 1)
                        .build())
                .header(HttpHeaders.USER_AGENT, USER_AGENT)
                .retrieve()
                .body(JsonNode.class);

        JsonNode firstResult = searchResponse == null
                ? objectMapper.createObjectNode()
                : searchResponse.path("query").path("search").path(0);

        String title = firstResult.path("title").asText("");

        if (!StringUtils.hasText(title)) {
            return Optional.empty();
        }

        return getWikipediaSummary(language, title);
    }

    private Optional<WikipediaArticle> getWikipediaSummary(String language, String title) {
        String encodedTitle = UriUtils.encodePathSegment(title, StandardCharsets.UTF_8);

        try {
            log.info("language = {}", language);
            log.info("title = {}", title);
            log.info("encodedTitle = {}", encodedTitle);

            JsonNode summary = restClient.get()
                    .uri("https://{language}.wikipedia.org/api/rest_v1/page/summary/{title}", language, title)
                    .header(HttpHeaders.USER_AGENT, USER_AGENT)
                    .retrieve()
                    .body(JsonNode.class);

            if (summary == null || !StringUtils.hasText(summary.path("extract").asText(""))) {
                return Optional.empty();
            }

            String sourceUrl = summary.path("content_urls").path("desktop").path("page").asText("");

            return Optional.of(new WikipediaArticle(
                    summary.path("title").asText(title),
                    summary.path("extract").asText(""),
                    sourceUrl
            ));

        } catch (HttpClientErrorException.NotFound notFound) {
            log.info("[위키백과 404] language={}, title={}", language, title);
            return Optional.empty();
        }
    }

    private JsonNode formatWithOpenAi(VisionCandidate candidate, GooglePlaceInfo placeInfo, WikipediaArticle article) {
        String wikipediaExtract = truncate(article.extract(), MAX_WIKIPEDIA_EXTRACT_LENGTH);

        String googlePlaceText = placeInfo == null ? "Google Places 정보 없음" : """
                장소명: %s
                주소: %s
                구글지도 링크: %s
                평점: %s
                리뷰 수: %s
                장소 유형: %s
                """.formatted(
                placeInfo.displayName(),
                placeInfo.formattedAddress(),
                placeInfo.googleMapsUri(),
                placeInfo.rating(),
                placeInfo.userRatingCount(),
                placeInfo.primaryType()
        );

        String prompt = """
                사진 인식 후보, Google Places 정보, 위키백과 정보를 바탕으로 문화유산 해설을 한국어로 정리해줘.

                반드시 JSON 객체만 반환해.
                키는 title, era, designation, overview, background, features, currentStatus만 사용해.

                작성 기준:
                - title은 Google Places 장소명을 우선 사용해.
                - era, background, features는 위키백과 정보를 우선 사용해.
                - 주소, 현재 운영 여부, 현재 관광지로서의 상태는 Google Places 정보를 참고해 currentStatus에 자연스럽게 반영해.
                - 제공된 정보에 없는 내용은 추측하지 말고 "확인 필요"라고 써.
                - 사용자가 여행 중 바로 읽을 수 있게 너무 딱딱하지 않게 작성해.

                사진 인식 후보:
                %s

                Google Places 정보:
                %s

                위키백과 제목:
                %s

                위키백과 요약:
                %s
                """.formatted(
                candidate.description(),
                googlePlaceText,
                article.title(),
                wikipediaExtract
        );

        Map<String, Object> requestBody = Map.of(
                "model", openAiModel,
                "response_format", Map.of("type", "json_object"),
                "messages", List.of(
                        Map.of("role", "system", "content", "너는 문화유산과 여행지 정보를 JSON으로 정리하는 서버 사이드 변환기야."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.2
        );

        try {
            JsonNode response = restClient.post()
                    .uri("https://api.openai.com/v1/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAiApiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(JsonNode.class);

            String content = response == null
                    ? "{}"
                    : response.path("choices")
                    .path(0)
                    .path("message")
                    .path("content")
                    .asText("{}");

            try {
                return objectMapper.readTree(content);
            } catch (IOException exception) {
                throw new IllegalArgumentException("OpenAI API 응답을 해석하지 못했습니다.", exception);
            }

        } catch (HttpClientErrorException e) {
            log.error("=== OpenAI API Error ===");
            log.error("Status: {}", e.getStatusCode());
            log.error("Response: {}", e.getResponseBodyAsString());
            throw e;
        }
    }

    private String toBase64(MultipartFile imageFile) {
        try {
            return Base64.getEncoder().encodeToString(imageFile.getBytes());
        } catch (IOException exception) {
            throw new IllegalArgumentException("이미지 파일을 읽지 못했습니다.");
        }
    }

    private PhotoAnalysisEntity failedAnalysis(PhotoEntity photo, String message) {
        return PhotoAnalysisEntity.builder()
                .photo(photo)
                .title("분석 실패")
                .era("확인 필요")
                .designation("확인 필요")
                .overview(message)
                .background("정확한 해설을 제공하기에 충분한 정보를 찾지 못했습니다.")
                .features("더 선명한 사진이나 대상 전체가 보이는 사진으로 다시 시도해주세요.")
                .currentStatus("분석 실패")
                .sourceName("PartTrip")
                .sourceUrl("")
                .photoAnalysisAccuracyCategory(PhotoAnalysisAccuracyCategory.FAILED_TO_IDENTIFY)
                .build();
    }

    private PhotoAnalysisAccuracyCategory toAccuracyCategory(double score) {
        if (score >= 0.8) {
            return PhotoAnalysisAccuracyCategory.HIGH_CONFIDENCE;
        }

        if (score >= 0.65) {
            return PhotoAnalysisAccuracyCategory.MEDIUM_CONFIDENCE;
        }

        return PhotoAnalysisAccuracyCategory.LOW_CONFIDENCE;
    }

    private String text(JsonNode node, String fieldName, String fallback) {
        String value = node.path(fieldName).asText("");
        return StringUtils.hasText(value) ? value : fallback;
    }

    private String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }

        return value.substring(0, maxLength);
    }

    private record VisionCandidate(String description, double score, String source) {
    }

    private record GooglePlaceInfo(
            String id,
            String displayName,
            String formattedAddress,
            String googleMapsUri,
            double rating,
            int userRatingCount,
            String primaryType
    ) {
    }

    private record WikipediaArticle(String title, String extract, String url) {
    }
}
