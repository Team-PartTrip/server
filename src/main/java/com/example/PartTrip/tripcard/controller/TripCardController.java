package com.example.PartTrip.tripcard.controller;

import com.example.PartTrip.tripcard.dto.request.DeleteTripCardsRequest;
import com.example.PartTrip.tripcard.dto.request.UpdateEntryCommentRequest;
import com.example.PartTrip.tripcard.dto.response.TripCardDetailResponse;
import com.example.PartTrip.tripcard.dto.response.TripCardEntryResponse;
import com.example.PartTrip.tripcard.dto.response.TripCardResponse;
import com.example.PartTrip.tripcard.service.TripCardEntryService;
import com.example.PartTrip.tripcard.service.TripCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// 여행 카드 (Func-003-02 ~ 05)
//
// 경로는 명세서(API-003-02 · 03 · 04 · 05 · 07)를 따라 /api/travel-cards 다.
// 테이블 이름(trip_card)과 다르지만, 앱이 명세서를 보고 붙기 때문에 명세서를 기준으로 둔다.
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/travel-cards")
public class TripCardController {

    private final TripCardService tripCardService;
    private final TripCardEntryService tripCardEntryService;

    // Func-003-02 여행 카드 목록 — 최근 여행순
    @GetMapping
    public List<TripCardResponse> getTripCards() {
        return tripCardService.getTripCards();
    }

    // Func-003-03 여행 카드 상세
    @GetMapping("/{cardId}")
    public TripCardDetailResponse getTripCard(@PathVariable Long cardId) {
        return tripCardService.getTripCard(cardId);
    }

    // Func-003-05 여행 카드 삭제 — 다중 선택
    @DeleteMapping
    public String deleteTripCard(@Valid @RequestBody DeleteTripCardsRequest request) {
        return tripCardService.deleteTripCard(request.getCardIds());
    }

    // Func-003-04 사진 추가 — EXIF 에서 촬영 시각과 좌표를 읽는다
    // comment 는 @RequestPart 가 아니라 @RequestParam 으로 받는다.
    // 앱·웹이 보내는 폼 필드에는 Content-Type 이 없어서 @RequestPart String 은
    // 415 로 떨어진다. 프로필 사진 업로드도 같은 방식을 쓰고 있다.
    @PostMapping(value = "/{cardId}/entries", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public TripCardEntryResponse addEntry(
            @PathVariable Long cardId,
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam(value = "comment", required = false) String comment
    ) {
        return tripCardEntryService.addEntry(cardId, imageFile, comment);
    }

    // Func-003-04 사진 코멘트 수정 — 사진을 올린 뒤에도 고칠 수 있어야 한다
    @PatchMapping("/{cardId}/entries/{entryId}")
    public TripCardEntryResponse updateEntryComment(
            @PathVariable Long cardId,
            @PathVariable Long entryId,
            @Valid @RequestBody UpdateEntryCommentRequest request
    ) {
        return tripCardEntryService.updateComment(cardId, entryId, request.getComment());
    }

    // Func-003-07 사진 삭제
    @DeleteMapping("/{cardId}/entries/{entryId}")
    public void deleteEntry(@PathVariable Long cardId, @PathVariable Long entryId) {
        tripCardEntryService.deleteEntry(cardId, entryId);
    }
}
