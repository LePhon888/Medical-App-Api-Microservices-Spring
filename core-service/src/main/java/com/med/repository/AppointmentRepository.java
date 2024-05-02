package com.med.repository;

import com.med.dto.AppointmentHourDTO;
import com.med.dto.AppointmentPatient;
import com.med.model.Appointment;
import com.med.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Query("SELECT a FROM Appointment a WHERE a.registerUser = :registerUser")
    List<Appointment> findByRegisterUser(@Param("registerUser") User registerUser);

    @Query("""
    SELECT new com.med.dto.AppointmentPatient(
        concat(a.user.lastName, ' ', a.user.firstName),
        a.user.image,
        a.reason,
        a.date,
        a.hour.hour   
    )
    FROM Appointment a 
    WHERE a.doctor.user.id = :userId AND a.isPaid = 1
    AND (:#{#params['startDate']} IS NULL OR a.date >= :#{#params['startDate']})
    AND (:#{#params['endDate']} IS NULL OR a.date <= :#{#params['endDate']})
    ORDER BY a.date DESC, CAST(a.hour.hour AS LOCALTIME) DESC
    """)
    List<AppointmentPatient> findAppointmentsByParams(Integer userId, @Param("params") Map<String, Object> params);


    @Modifying(clearAutomatically = true)
    @Query("UPDATE Appointment a SET a.isConfirm = :isConfirm WHERE a.id = :id")
    int update(@Param("id") Integer id, @Param("isConfirm") Short isConfirm);

    @Query("SELECT a FROM Appointment a WHERE a.registerUser.id = :id or a.user.id = :id or a.doctor.user.id = :id ORDER BY a.id DESC")
    List<Appointment> findByUserId(@Param("id") Integer id);

    @Query("SELECT Count(*) " +
            "FROM Appointment a " +
            "WHERE a.isPaid = 1 AND a.isConfirm = 1 AND a.user.id = :userId AND a.doctor.id = :doctorId")
    Long countAppointmentsByUserId(String userId, String doctorId);

    @Query("SELECT new com.med.dto.AppointmentHourDTO(a.hour.id) FROM Appointment a WHERE a.isPaid = 1 AND a.isConfirm = 1 AND (:date is null or a.date = :date) AND (:doctorId is null or a.doctor.id = :doctorId) ORDER BY a.hour.id")
    List<AppointmentHourDTO> getAppointmentHourByDate(LocalDate date, Integer doctorId);
}
