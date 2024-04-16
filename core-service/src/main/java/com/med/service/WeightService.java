package com.med.service;

import com.med.dto.WeightDTO;
import com.med.model.Weight;
import com.med.repository.WeightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class WeightService {
    @Autowired
    private WeightRepository weightRepository;
    private LocalDate currentDate = LocalDate.now();


    private LocalDate startDateWeek = currentDate.minusWeeks(1);


    private LocalDate startDateMonth = currentDate.minusMonths(1);


    public List<WeightDTO> getWeightsByUserId(Integer userId) {
        return weightRepository.getWeightsByUserId(userId);
    }

    public List<WeightDTO> getWeightsByUserIdWeek(Integer userId) {
        return weightRepository.getWeightsByUserIdWeek(userId, startDateWeek);
    }

    public List<WeightDTO> getWeightsByUserIdMonth(Integer userId) {
        return weightRepository.getWeightsByUserIdMonth(userId, startDateMonth);
    }

    public List<WeightDTO> getNewWeight(Integer userId) {
        System.out.println("startDateWeekstartDateWeek " + startDateWeek + " " + startDateMonth);
        return weightRepository.getNewWeight(userId, PageRequest.of(0, 1));
    }

    public List<Map<String, Double>> averageWeight(Integer userId) {
        List<WeightDTO> weekWeights = this.getWeightsByUserIdWeek(userId);
        List<WeightDTO> monthWeights = this.getWeightsByUserIdMonth(userId);
        double weekSum = 0;
        double monthSum = 0;
        for (WeightDTO weight : weekWeights) {
            weekSum += weight.getNumber();
        }
        for (WeightDTO weight : monthWeights) {
            monthSum += weight.getNumber();
        }
        double weekAvg = weekSum / weekWeights.size();
        double monthAvg = monthSum / monthWeights.size();

        return List.of(Map.of("week", Math.ceil(weekAvg * 100) / 100, "month", Math.ceil(monthAvg * 100) / 100));
    }

    public Weight create(Weight weight) {
        return weightRepository.save(weight);
    }

    public WeightDTO saveWeight(Weight newWeight) {
        Weight weight = weightRepository.save(newWeight);
        WeightDTO weightDTO = new WeightDTO(weight.getId(), weight.getNumber(), weight.getDate(), weight.getUser().getId(), weight.getTime(), weight.getBmi(), weight.getBmr(), weight.getHeight(), weight.getClassification());
        return weightDTO;
    }
}
