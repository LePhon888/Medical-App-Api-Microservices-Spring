package com.med.service;

import com.med.dto.WeightDTO;
import com.med.model.Weight;
import com.med.repository.WeightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class WeightService {
    @Autowired
    private WeightRepository weightRepository;
    private LocalDate currentDate = LocalDate.now();


    private LocalDate startDateWeek = currentDate.minusDays(7);

    private LocalDate startDateMonth = currentDate.minusDays(30);


    public List<WeightDTO> getWeightsByUserId(Integer userId) {
        return weightRepository.getWeightsByUserId(userId);
    }

    public List<WeightDTO> getWeightsByUserIdWeek(Integer userId) {
        return weightRepository.getWeightsByUserIdWeek(userId, startDateWeek);
    }

    public List<WeightDTO> getWeightsByUserIdMonth(Integer userId) {
        return weightRepository.getWeightsByUserIdMonth(userId, startDateMonth);
    }
}
