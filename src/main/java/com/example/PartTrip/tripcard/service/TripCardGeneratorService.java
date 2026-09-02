package com.example.PartTrip.tripcard.service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

public interface TripCardGeneratorService {
    @Transactional
    int closeCardsBefore(LocalDate date);
}