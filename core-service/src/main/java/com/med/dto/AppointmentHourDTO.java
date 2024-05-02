package com.med.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AppointmentHourDTO {
    private String id;
    public AppointmentHourDTO(Integer id) {
        this.id = id.toString();
    }
}
