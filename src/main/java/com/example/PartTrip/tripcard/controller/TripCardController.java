package com.example.PartTrip.tripcard.controller;

import com.example.PartTrip.tripcard.dto.response.TripCardDetailResponse;
import com.example.PartTrip.tripcard.dto.response.TripCardEntryResponse;
import com.example.PartTrip.tripcard.dto.response.TripCardResponse;
import com.example.PartTrip.tripcard.service.TripCardEntryService;
import com.example.PartTrip.tripcard.service.TripCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/trip-cards")
@RequiredArgsConstructor
public class TripCardController {

    private final TripCardService tripCardService;
    private final TripCardEntryService tripCardEntryService;


    @GetMapping
    public List<TripCardResponse> getTripCards() {
        return tripCardService.getTripCards();
    }


    @GetMapping("/{tripCardId}")
    public TripCardDetailResponse getTripCard(@PathVariable Long tripCardId) {
        return tripCardService.getTripCard(tripCardId);
    }


    @DeleteMapping
    public String deleteTripCard(@RequestBody Set<Long> tripCardIds) {
        return tripCardService.deleteTripCard(tripCardIds);
    }


    @PostMapping("/{cardId}/entries")
    public TripCardEntryResponse addEntry(@PathVariable Long cardId,
             @RequestPart("imageFile") MultipartFile imageFile,
             @RequestPart(value = "comment", required = false) String comment
    ) {
        return tripCardEntryService.addEntry(cardId, imageFile, comment);
    }

    @DeleteMapping("/{cardId}/entries/{entryId}")
    public void deleteEntry(@PathVariable Long cardId, @PathVariable Long entryId) {
        tripCardEntryService.deleteEntry(cardId, entryId);
    }

}
