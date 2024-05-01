package com.medical.medicationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicationScheduleGroupDTO {
    private Integer id;
    private Integer userId;
    private String groupName;
    private String hospitalName;
    private String doctorName;
    private Boolean isActive;
    private List<CreateMedicationScheduleDTO> medicineList;
}
