package com.med.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AppointmentPatient {
    private String patientName;
    private String patientImageUrl;
    private String reason;
    private Date date;
    private String hour;
}
