package com.medical.medicationservice.repository;

import com.medical.medicationservice.dto.MedicationScheduleDetailProjection;
import com.medical.medicationservice.dto.ScheduleTimeDTO;
import com.medical.medicationservice.model.ScheduleTime;
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
        s.id as scheduleTimeId,
        msg.name as groupName
    FROM 
        `medication-service`.medication_schedule ms
    INNER JOIN 
        `medication-service`.schedule_time s ON ms.id = s.medication_schedule_id
    INNER JOIN 
        `medication-service`.medicine m on ms.medicine_id = m.id
    INNER JOIN 
        `medication-service`.medicine_unit mu on ms.unit_id = mu.id
    LEFT JOIN 
        `medication-service`.schedule_time_detail dt ON dt.schedule_time_id = s.id  AND dt.date = :startDate
    LEFT JOIN
        `medication-service`.medication_schedule_group msg on ms.group_id = msg.id
    WHERE 
        ms.user_id = :userId
        AND ms.is_active = TRUE
        AND DATEDIFF(:startDate, ms.start_date) >= 0
        AND (
            -- If frequency = 1, this is daily mode we will select for daily
            (ms.frequency = 1) 
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
    SELECT new com.medical.medicationservice.dto.ScheduleTimeDTO(s.id, s.time, s.quantity)
    FROM ScheduleTime s 
    WHERE s.medicationSchedule.id = :scheduleId
    ORDER BY s.time
    """)
    List<ScheduleTimeDTO> getScheduleTimeByMedicationScheduleId(Integer scheduleId);



    @Query(value = """ 
    SELECT
         ms.id as id,
         ms.user_id as userId,
         m.name as medicineName,
         mu.name as unitName,
         MIN(s.time) as time,
         s.quantity,
         s.id as scheduleTimeId,
         CAST((TIMESTAMPADD(HOUR, 7, CURRENT_TIMESTAMP)) AS DATE) as date
     FROM
         `medication-service`.medication_schedule ms
     INNER JOIN
         `medication-service`.schedule_time s
             ON ms.id = s.medication_schedule_id 
                     AND TIMESTAMPDIFF(MINUTE, TIMESTAMPADD(HOUR, 7, CURRENT_TIME), s.time) BETWEEN 0 AND 1
     INNER JOIN
         `medication-service`.medicine m on ms.medicine_id = m.id
     INNER JOIN
         `medication-service`.medicine_unit mu on ms.unit_id = mu.id
     LEFT JOIN
         `medication-service`.schedule_time_detail dt
             ON dt.schedule_time_id = s.id AND dt.date = CAST(TIMESTAMPADD(HOUR, 7, CURRENT_TIMESTAMP) AS DATE)
             AND (dt.is_used IS NULL OR dt.is_used = 0)
     WHERE
         ms.is_active = TRUE
       AND DATEDIFF(CAST(TIMESTAMPADD(HOUR, 7, CURRENT_TIMESTAMP) AS DATE), ms.start_date) >= 0
       AND (
             (ms.frequency = 1)
             OR
             (ms.frequency > 1 AND DATEDIFF(ms.start_date, TIMESTAMPADD(HOUR, 7, CURRENT_DATE)) % ms.frequency = 0)
             OR
             (ms.frequency IS NULL AND FIND_IN_SET(DAYOFWEEK(TIMESTAMPADD(HOUR, 7, CURRENT_DATE)), ms.selected_days) > 0)
         )
     GROUP BY
         id,
         user_id,
         medicineName,
         unitName,
         s.quantity,
         scheduleTimeId,
         date
     ORDER BY
         time
    """, nativeQuery = true)
    List<MedicationScheduleDetailProjection> getScheduleTimeToSendNotification();

}
