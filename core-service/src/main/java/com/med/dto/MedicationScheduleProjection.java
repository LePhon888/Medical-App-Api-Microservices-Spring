package com.med.dto;

import java.time.LocalDateTime;

public interface MedicationScheduleProjection {
    Integer getId();
    String getMedicineName();
    LocalDateTime getDateTime();
    Boolean getIsActive();
}
