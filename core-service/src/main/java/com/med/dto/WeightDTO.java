package com.med.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class WeightDTO {
    private int id;
    private float number;
    private LocalDate date;
    private int userId;
    private LocalTime time;
    private Float bmi;
    private Float bmr;
    private Float height;
    private String classification;
}
