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
@Data
public class CreateOrUpdateMedicationScheduleDTO {
    private Integer id;
    private Integer userId;
    private Medicine medicine;
    private MedicineUnit medicineUnit;
    private LocalDate startDate;
    private Integer frequency;
    private List<Integer> selectedDays;
    private Boolean isActive;
    private List<ScheduleTimeDTO> scheduleTimes;
}

