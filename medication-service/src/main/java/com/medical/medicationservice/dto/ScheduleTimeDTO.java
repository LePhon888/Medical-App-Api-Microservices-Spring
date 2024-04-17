package com.medical.medicationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleTimeDTO {
    private Integer id;
    private LocalTime time;
    private BigDecimal quantity;
}

