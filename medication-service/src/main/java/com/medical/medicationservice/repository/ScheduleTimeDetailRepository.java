package com.medical.medicationservice.repository;

import com.medical.medicationservice.dto.HistoryMedication;
import com.medical.medicationservice.model.ScheduleTimeDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

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

    /* Function to check whether the user has interacted by clicking skipped or used medicine
    If return null which mean no interacted yet, 0 is marked as skipped, 1 is marked as used
    */
    @Query("""
    SELECT dt.isUsed
    FROM ScheduleTimeDetail dt 
    WHERE dt.scheduleTime.medicationSchedule.id = :medicationScheduleId 
    AND dt.scheduleTime.id = :scheduleTimeId
    AND dt.date = :date 
    """)
    Boolean checkMedicationInteraction(Integer medicationScheduleId, Integer scheduleTimeId, LocalDate date);
}

