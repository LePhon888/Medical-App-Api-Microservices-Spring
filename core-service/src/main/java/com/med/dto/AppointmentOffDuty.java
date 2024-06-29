package com.med.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentOffDuty {
    private Integer id;
    private Date date;
    private String hour;
}
