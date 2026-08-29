package com.example.PartTrip.tripcard.service;

import com.example.PartTrip.tripcard.dto.response.TripCardEntryResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface TripCardEntryService {
    @Transactional
    TripCardEntryResponse addEntry(Long cardId, MultipartFile imageFile, String comment);

    @Transactional
    TripCardEntryResponse updateComment(Long cardId, Long entryId, String comment);

    @Transactional
    void deleteEntry(Long cardId, Long entryId);
}
