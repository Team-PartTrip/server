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

    @Value("${openai.api-key:${OPENAI_API_KEY:}}")
    private String openAiApiKey;

    @Value("${openai.model:gpt-4o-mini}")
    private String openAiModel;

    @Override
    public PhotoAnalysisEntity analyze(PhotoEntity photo, MultipartFile imageFile) {
        if (!StringUtils.hasText(googleVisionApiKey) || !StringUtils.hasText(openAiApiKey)) {
            return mockPhotoAnalyzer.analyze(photo, imageFile);
        }

        try {
            VisionCandidate candidate = recognizeImage(imageFile);

            // ▼▼▼ Vision API 인식 결과를 콘솔에서 확인하기 위한 로그 ▼▼▼
            log.info("[Vision 인식 결과] description='{}', score={}", candidate.description(), candidate.score());
            // ▲▲▲

            if (candidate.score() < MIN_CONFIDENCE) {
                return failedAnalysis(photo, "사진 속 대상을 정확하게 특정하지 못했습니다.");
            }

            WikipediaArticle article = findWikipediaArticle(candidate.description())
                    .orElseGet(() -> findWikipediaArticle(candidate.description() + " 문화재").orElse(null));
            if (article == null) {
                return failedAnalysis(photo, "사진 속 대상과 일치하는 위키백과 정보를 찾지 못했습니다.");
            }

            JsonNode formatted = formatWithOpenAi(candidate, article);
            return PhotoAnalysisEntity.builder()
                    .photo(photo)
                    .title(text(formatted, "title", article.title()))
                    .era(text(formatted, "era", "확인 필요"))
                    .designation(text(formatted, "designation", "확인 필요"))
                    .overview(text(formatted, "overview", article.extract()))
                    .background(text(formatted, "background", "확인 필요"))
                    .features(text(formatted, "features", "확인 필요"))
                    .currentStatus(text(formatted, "currentStatus", "확인 필요"))
                    .sourceName("Wikipedia")
                    .sourceUrl(article.url())
                    .photoAnalysisAccuracyCategory(toAccuracyCategory(candidate.score()))
                    .build();
        } catch (RuntimeException exception) {
            exception.printStackTrace();
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

        // ▼▼▼ Vision API 원본 응답 전체를 보고 싶으면 이 줄 주석 해제 ▼▼▼
        // log.info("[Vision 원본 응답] {}", response);
        // ▲▲▲

        JsonNode firstResponse = response == null ? objectMapper.createObjectNode() : response.path("responses").path(0);
        if (!firstResponse.path("error").isMissingNode()) {
            throw new IllegalArgumentException("Google Vision API 분석에 실패했습니다.");
        }

        List<VisionCandidate> candidates = new ArrayList<>();
        addCandidates(candidates, firstResponse.path("landmarkAnnotations"), "description");
        addCandidates(candidates, firstResponse.path("webDetection").path("webEntities"), "description");
        addCandidates(candidates, firstResponse.path("labelAnnotations"), "description");
        addBestGuessCandidates(candidates, firstResponse.path("webDetection").path("bestGuessLabels"));

        // ▼▼▼ 인식된 모든 후보를 보고 싶으면 이 로그 확인 ▼▼▼
        log.info("[Vision 전체 후보] {}", candidates);
        // ▲▲▲

        return candidates.stream()
                .filter(candidate -> StringUtils.hasText(candidate.description()))
                .max(Comparator.comparingDouble(VisionCandidate::score))
                .orElse(new VisionCandidate("", 0.0));
    }

    private void addCandidates(List<VisionCandidate> candidates, JsonNode nodes, String descriptionField) {
        if (!nodes.isArray()) {
            return;
        }
        nodes.forEach(node -> candidates.add(new VisionCandidate(
                node.path(descriptionField).asText(""),
                node.path("score").asDouble(0.0)
        )));
    }

    private void addBestGuessCandidates(List<VisionCandidate> candidates, JsonNode nodes) {
        if (!nodes.isArray()) {
            return;
        }
        nodes.forEach(node -> candidates.add(new VisionCandidate(node.path("label").asText(""), 0.65)));
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
            JsonNode summary = restClient.get()
                    .uri("https://{language}.wikipedia.org/api/rest_v1/page/summary/{title}", language, encodedTitle)
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
            // 위키백과 요약 문서가 없는 경우(리다이렉트/중의성 등) -> 실패로 죽이지 말고 빈 결과로 처리
            log.info("[위키백과 404] language={}, title={}", language, title);
            return Optional.empty();
        }
    }

    private JsonNode formatWithOpenAi(VisionCandidate candidate, WikipediaArticle article) {
        String wikipediaExtract = truncate(article.extract(), MAX_WIKIPEDIA_EXTRACT_LENGTH);
        String prompt = """
                사진 인식 후보와 위키백과 정보를 바탕으로 문화유산 해설을 한국어로 정리해줘.
                반드시 JSON 객체만 반환하고, 키는 title, era, designation, overview, background, features, currentStatus만 사용해.
                정보가 위키백과 원문에 없으면 추측하지 말고 "확인 필요"라고 써.

                사진 인식 후보: %s
                위키백과 제목: %s
                위키백과 요약:
                %s
                """.formatted(candidate.description(), article.title(), wikipediaExtract);

        Map<String, Object> requestBody = Map.of(
                "model", openAiModel,
                "response_format", Map.of("type", "json_object"),
                "messages", List.of(
                        Map.of("role", "system", "content", "너는 문화유산 해설 정보를 섹션별 JSON으로 정리하는 서버 사이드 변환기야."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.2
        );

        JsonNode response = restClient.post()
                .uri("https://api.openai.com/v1/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAiApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(JsonNode.class);

        String content = response == null
                ? "{}"
                : response.path("choices").path(0).path("message").path("content").asText("{}");
        try {
            return objectMapper.readTree(content);
        } catch (IOException exception) {
            throw new IllegalArgumentException("OpenAI API 응답을 해석하지 못했습니다.");
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

    private record VisionCandidate(String description, double score) {
    }

    private record WikipediaArticle(String title, String extract, String url) {
    }
}