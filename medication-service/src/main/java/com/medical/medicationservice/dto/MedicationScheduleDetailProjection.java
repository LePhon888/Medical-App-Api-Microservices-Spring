package com.medical.medicationservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public interface MedicationScheduleDetailProjection {
    Integer getId();
    Integer getUserId();
    String getMedicineName();
    String getUnitName();
    LocalTime getTime();
    BigDecimal getQuantity();
    @Value("#{target.isUsed == null ? null : target.isUsed == 1 ? true : false}")
    Boolean getIsUsed();
    Integer getScheduleTimeId();
    String getGroupName();
    LocalDate getDate();
}
