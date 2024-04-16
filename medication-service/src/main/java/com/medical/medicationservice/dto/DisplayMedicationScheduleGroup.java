package com.medical.medicationservice.dto;

import com.medical.medicationservice.model.Medicine;
import com.medical.medicationservice.model.MedicineUnit;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Data
public class DisplayMedicationScheduleGroup {
    private Integer id;
    private Integer userId;
    private String groupName;
    private String hospitalName;
    private String doctorName;
    private Boolean isActive;
    private List<CreateMedicationScheduleDTO> medicationScheduleDTOList;
}
