package com.med.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
@JsonSerialize
public interface MedicationScheduleDetailProjection extends Serializable {
    Integer getId();
    String getMedicineName();
    String getUnitName();
    LocalTime getTime();
    BigDecimal getQuantity();
    Boolean getIsUsed();
    Integer getScheduleTimeId();
}
