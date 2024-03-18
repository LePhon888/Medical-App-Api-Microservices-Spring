package com.med.repository;

import com.med.dto.HistoryMedication;
import com.med.dto.MedicationScheduleDetailProjection;
import com.med.model.ScheduleTime;
import com.med.model.ScheduleTimeDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ScheduleTimeDetailRepository extends JpaRepository<ScheduleTimeDetail, Integer> {
    @Query("""
    SELECT new com.med.dto.HistoryMedication(
        dt.date,
        dt.scheduleTime.time,
        dt.scheduleTime.medicationSchedule.medicine.name,
        dt.scheduleTime.quantity,
        dt.isUsed
    )
    FROM ScheduleTimeDetail dt
    WHERE dt.scheduleTime.medicationSchedule.user.id = :userId
    ORDER BY dt.date DESC, dt.scheduleTime.time DESC
    """)
    Page<HistoryMedication> getHistoryMedicationByUserId (Integer userId, Pageable pageable);
}
