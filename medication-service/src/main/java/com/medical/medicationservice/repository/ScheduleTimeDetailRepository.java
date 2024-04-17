package com.medical.medicationservice.repository;

import com.medical.medicationservice.dto.HistoryMedication;
import com.medical.medicationservice.model.ScheduleTimeDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleTimeDetailRepository extends JpaRepository<ScheduleTimeDetail, Integer> {
    @Query("""
    SELECT new com.medical.medicationservice.dto.HistoryMedication(
        dt.date,
        dt.scheduleTime.time,
        dt.scheduleTime.medicationSchedule.medicine.name,
        dt.scheduleTime.quantity,
        dt.isUsed
    )
    FROM ScheduleTimeDetail dt
    WHERE dt.scheduleTime.medicationSchedule.userId = :userId
    ORDER BY dt.date DESC, dt.scheduleTime.time DESC
    """)
    Page<HistoryMedication> getHistoryMedicationByUserId (Integer userId, Pageable pageable);
}
