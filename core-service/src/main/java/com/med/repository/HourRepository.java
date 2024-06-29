package com.med.repository;

import com.med.model.Hour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface HourRepository extends JpaRepository<Hour, Integer> {

    @Query(value = """
        SELECT h.*
         FROM hour h
         WHERE
            -- Select hour that greater than current time
           (:date > TIMESTAMPADD(HOUR, 7, CURRENT_DATE)
           OR TIME(h.hour) > TIMESTAMPADD(HOUR, 7, CURRENT_TIME))
            -- Select hour that not added yet into database by doctor and date
           AND h.id NOT IN (
             SELECT a.hour_id
             FROM appointment a
             WHERE a.date = :date AND a.doctor_id = :doctorId
         )
   \s""", nativeQuery = true)
    List<Hour> getOffDutyHoursByDoctorIdAndDate(@Param("doctorId") Integer doctorId, @Param("date") LocalDate date);

}
