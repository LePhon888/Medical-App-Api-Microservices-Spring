package com.med.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryMedication {
    private LocalDate date;
    private LocalTime time;
    private String medicineName;
    private BigDecimal quantity;
    private Boolean isUsed;
}
