package com.med.dto;

import com.med.model.Medicine;
import com.med.model.MedicineUnit;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ScheduleTimeDetailDTO {
    private Integer id;
    private LocalDate date;
    private Boolean isUsed;
    private Integer scheduleTimeId;
}

