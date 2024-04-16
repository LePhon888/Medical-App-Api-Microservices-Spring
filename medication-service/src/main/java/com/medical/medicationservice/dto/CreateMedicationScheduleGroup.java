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
public class CreateMedicationScheduleGroup {
    private Integer id;
    private Integer userId;
    private String groupName;
    private String hospitalName;
    private String doctorName;
    private Boolean isActive;
    private List<CreateMedicationScheduleDTO> medicationScheduleDTOList;

    public CreateMedicationScheduleGroup(Integer id, Integer userId, String groupName, String hospitalName, String doctorName, Boolean isActive) {
        this.id = id;
        this.userId = userId;
        this.groupName = groupName;
        this.hospitalName = hospitalName;
        this.doctorName = doctorName;
        this.isActive = isActive;
    }
}
