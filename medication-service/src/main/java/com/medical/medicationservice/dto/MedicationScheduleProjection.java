package com.medical.medicationservice.dto;

import java.time.LocalDateTime;

public interface MedicationScheduleProjection {
    Integer getId();
    String getMedicineName();
    LocalDateTime getDateTime();
    Boolean getIsActive();
    Integer getGroupId();
    String getGroupName();
}
