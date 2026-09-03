package com.example.PartTrip.tripcard.service;

import java.time.LocalDate;

public interface TripCardGeneratorService {
    int closeCardsBefore(LocalDate date);
}
