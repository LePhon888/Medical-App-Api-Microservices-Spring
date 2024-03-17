package com.med.repository;

import com.med.dto.MedicationScheduleDTO;
import com.med.dto.MedicationScheduleProjection;
import com.med.model.MedicationSchedule;
import com.med.model.Medicine;
import com.med.model.MedicineUnit;
import com.med.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MedicationScheduleRepository extends JpaRepository<MedicationSchedule, Integer> {
    Optional<MedicationSchedule> findByUserAndMedicineAndMedicineUnit(User user, Medicine medicine, MedicineUnit medicineUnit);
    @Query(value = """
    SELECT
    IFNULL(
            ( -- Check if there are time that greater than current date time
                SELECT
                    TIMESTAMP(CAST(TIMESTAMPADD(HOUR, 7, CURRENT_TIMESTAMP) AS DATE), s.time)
                FROM
                    medicalapp.schedule_time s
                WHERE
                    s.medication_schedule_id = ms.id
                  AND ms.start_date <= CAST(TIMESTAMPADD(HOUR,7, CURRENT_TIMESTAMP) AS DATE)
                  AND s.time > TIMESTAMPADD(HOUR,7, CURRENT_TIME)
                  AND (
                      -- This is for the daily so we only compare the time part
                      (ms.frequency = 1)
                       OR
                       -- Check if the current date is belong to the frequency
                      (ms.frequency > 1 AND DATEDIFF(CAST(TIMESTAMPADD(HOUR,7, CURRENT_TIMESTAMP) AS DATE), ms.start_date) % ms.frequency = 0)
                       OR
                        -- Check if the current date in schedule selected days
                      (ms.frequency IS NULL AND FIND_IN_SET(DAYOFWEEK(CAST(TIMESTAMPADD(HOUR,7, CURRENT_TIMESTAMP) AS DATE)), ms.selected_days) > 0)
                  )
                ORDER BY s.time
                LIMIT 1
            ),
            ( -- Move to get the next occurrences (the future date)
                SELECT
                    CASE
                    -- For the frequency = 1, pick the greatest date between startDate and current date after that add 1 more day
                    WHEN frequency = 1 THEN 
                        CASE 
                            -- If the current date greater than start date, add 1 day to current date
                            WHEN ms.start_date <= CAST(TIMESTAMPADD(HOUR,7, CURRENT_TIMESTAMP)  AS DATE)
                                    THEN TIMESTAMP(CAST(TIMESTAMPADD(DAY ,1, CURRENT_TIMESTAMP) AS DATE), s.time)
                            ELSE  
                            -- If the current date less than start date, select start date
                                TIMESTAMP(ms.start_date, s.time)
                        END
                    WHEN frequency > 1 THEN (
                        -- Loop through each date with interval frequency
                        WITH RECURSIVE DateSequence AS (
                            SELECT ms.start_date AS x
                            UNION ALL
                            SELECT x + INTERVAL frequency DAY
                            FROM DateSequence
                            WHERE x <= TIMESTAMPADD(DAY, frequency, CURRENT_DATE)
                        )
                        /* Select the first date greater than the current date
                        and check if meet the condition for frequency: date diff and modulus%frequency
                        Ex: Feb 27. Frequency: 3. So the date valid is March 1, March 4
                        */
                        SELECT TIMESTAMP(x,s.time) AS result
                        FROM DateSequence
                        WHERE x > CAST(TIMESTAMPADD(HOUR,7, CURRENT_TIMESTAMP) AS DATE)
                          AND DATEDIFF(ms.start_date, x) % frequency = 0
                        LIMIT 1
                    )
                    
                     WHEN frequency IS NULL THEN (
                        -- Loop through each date
                        WITH RECURSIVE DateSequence AS (
                            SELECT 
                                GREATEST(ms.start_date, CAST(TIMESTAMPADD(HOUR,7, CURRENT_TIMESTAMP) AS DATE)) AS x
                            UNION ALL
                            SELECT x + INTERVAL 1 DAY
                            FROM DateSequence
                            WHERE x <= TIMESTAMPADD(DAY, 7, GREATEST(CURRENT_DATE, ms.start_date))
                        )
                        /* Select the first date greater than the current date and in selected days
                        we use date of week to return a number in range 0-7. 
                        Based on this check if the date in selected days.
                        */
                        SELECT TIMESTAMP(x, s.time) AS result
                        FROM DateSequence
                        WHERE x > CAST(TIMESTAMPADD(HOUR,7, CURRENT_TIMESTAMP) AS DATE)
                          AND FIND_IN_SET(DAYOFWEEK(x), ms.selected_days) > 0
                          AND x >= ms.start_date
                        LIMIT 1
                    )
                    END
                FROM
                    medicalapp.schedule_time s
                WHERE
                    s.medication_schedule_id = ms.id
                ORDER BY s.time
                LIMIT 1
            )
        ) as dateTime,
    ms.id AS id,
    m.name AS medicineName,
    ms.is_active as isActive
    FROM
        medicalapp.medication_schedule ms
        INNER JOIN medicalapp.medicine m ON ms.medicine_id = m.id
    WHERE
        ms.user_id = :userId
        AND ms.is_active = true
    ORDER BY
        dateTime 
        """, nativeQuery = true)
    List<MedicationScheduleProjection> getMedicationScheduleByUserId(@Param("userId") Integer userId);

    @Query("""
    SELECT 
        ms.id as id,
        ms.isActive as isActive,
        ms.medicine.name as medicineName
    FROM 
        MedicationSchedule ms
    WHERE 
        ms.isActive = false
        AND ms.user.id = :userId 
    """)
    List<MedicationScheduleProjection> getInactiveMedicationScheduleByUserId(@Param("userId") Integer userId);

    @Query("""
    SELECT new com.med.dto.MedicationScheduleDTO(
        ms.user.id,
        ms.medicine,
        ms.medicineUnit,
        ms.startDate,
        ms.frequency,
        ms.selectedDays,
        ms.isActive
    )
    FROM
        MedicationSchedule ms
    WHERE 
        ms.id = :id
    """)
    MedicationScheduleDTO getMedicationScheduleById(Integer id);
}
