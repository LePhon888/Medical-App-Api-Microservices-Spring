package com.medical.medicationservice.dto;

import com.medical.medicationservice.model.Medicine;
import com.medical.medicationservice.model.MedicineUnit;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMedicationScheduleDTO {
    private Integer id;
    private Integer userId;
    private Medicine medicine;
    private MedicineUnit medicineUnit;
    private LocalDate startDate;
    private Integer frequency;
    private String selectedDays;
    private Boolean isActive;
    private Integer groupId;
    private List<ScheduleTimeDTO> scheduleTimes;
}

