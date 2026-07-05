package com.example.PartTrip.service.main;

import com.example.PartTrip.repository.main.CountryInfoRepository;
import com.example.PartTrip.repository.main.FestivalRepository;
import com.example.PartTrip.repository.main.PopulationInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainService {

    private final CountryInfoRepository countryInfoRepository;
    private final PopulationInfoRepository populationInfoRepository;
    private final FestivalRepository festivalRepository;

}
