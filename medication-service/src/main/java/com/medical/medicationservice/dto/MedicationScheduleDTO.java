package com.medical.medicationservice.dto;
import com.medical.medicationservice.model.Medicine;
import com.medical.medicationservice.model.MedicineUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
public class MedicationScheduleDTO {
    private Integer id;
    private Integer userId;
    private Medicine medicine;
    private MedicineUnit medicineUnit;
    private LocalDate startDate;
    private Integer frequency;
    private String selectedDays;
    private Boolean isActive;
    private Integer groupId;
    private String groupName;
    private List<ScheduleTimeDTO> scheduleTimes;

    public MedicationScheduleDTO(Integer id, Integer userId, Medicine medicine, MedicineUnit medicineUnit, LocalDate startDate, Integer frequency, String selectedDays, Boolean isActive, Integer groupId, String groupName) {
        this.userId = userId;
        this.medicine = medicine;
        this.medicineUnit = medicineUnit;
        this.startDate = startDate;
        this.frequency = frequency;
        this.selectedDays = selectedDays;
        this.isActive = isActive;
        this.groupId = groupId;
        this.groupName = groupName;
    }
}

