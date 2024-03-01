package com.med.repository;

import com.med.dto.MedicationScheduleDetailProjection;
import com.med.dto.ScheduleTimeDTO;
import com.med.model.ScheduleTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleTimeRepository extends JpaRepository<ScheduleTime, Integer> {
    @Query(value = """ 
    SELECT
        IFNULL(dt.id, 0) as id, 
        m.name as medicineName, 
        mu.name as unitName, 
        s.time as time, 
        s.quantity,
        dt.is_used as isUsed,
        s.id as scheduleTimeId
    FROM 
        medicalapp.medication_schedule ms
    INNER JOIN 
        medicalapp.schedule_time s ON ms.id = s.medication_schedule_id
    INNER JOIN 
        medicalapp.medicine m on ms.medicine_id = m.id
    INNER JOIN 
        medicalapp.medicine_unit mu on ms.unit_id = mu.id
    LEFT JOIN 
        medicalapp.schedule_time_detail dt ON dt.schedule_time_id = s.id  AND dt.date = :startDate
    WHERE 
        ms.user_id = :userId
        AND ms.is_active = TRUE
        AND (
            -- If frequency = 1, this is daily mode we will select for daily
            (ms.frequency = 1 AND ms.start_date <= :startDate) 
            OR 
            /* If frequency > 1, we will compare the day of selected date and start date = frequency
            Ex: StartDate = Feb 25 & frequency = 2. Then the schedule will show when the date is 27, 29, 31,...
            */
            (ms.frequency > 1 AND DATEDIFF(ms.start_date, :startDate) % ms.frequency = 0)
            OR
             /* If frequency IS NULL, we will check if t he DayOfWeek of selected date in SelectedDays
             Ex: StartDate = Feb 19 -> Return 2 (Monday) in SelectedDays(1, 2, 3, 4)
             */
            (ms.frequency IS NULL AND FIND_IN_SET(DAYOFWEEK(:startDate), ms.selected_days) > 0)
        )
            ORDER BY s.time
    """, nativeQuery = true)
    List<MedicationScheduleDetailProjection> getScheduleTime(@Param("userId") Integer userId, @Param("startDate") LocalDate startDate);


    @Query("""
    SELECT new com.med.dto.ScheduleTimeDTO(s.id, s.time, s.quantity)
    FROM ScheduleTime s 
    WHERE s.medicationSchedule.id = :scheduleId
    ORDER BY s.time
    """)
    List<ScheduleTimeDTO> getScheduleTimeByMedicationScheduleId(Integer scheduleId);

}
