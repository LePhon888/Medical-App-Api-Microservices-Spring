package com.med.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface MedicationScheduleDetailProjection {
    Integer getId();
    String getMedicineName();
    String getUnitName();
    LocalTime getTime();
    BigDecimal getQuantity();
    Boolean getIsUsed();
    Integer getScheduleTimeId();
}
