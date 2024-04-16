package com.medical.medicationservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ScheduleTimeDetailDTO {
    private Integer id;
    private LocalDate date;
    private Boolean isUsed;
    private Integer scheduleTimeId;
}

