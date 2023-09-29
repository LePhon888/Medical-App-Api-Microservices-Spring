package com.med.repository;

import com.med.model.Appointment;
import com.med.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Query("SELECT a FROM Appointment a WHERE a.registerUser = :registerUser")
    List<Appointment> findByRegisterUser(@Param("registerUser") User registerUser);

    @Query("SELECT a FROM Appointment a WHERE " +
            "(:#{#params['date']} IS NULL OR STR_TO_DATE(:#{#params['date']}, '%Y-%m-%d') = a.date) AND " +
            "(:#{#params['kw']} IS NULL OR a.user.firstName = :#{#params['param2']})")
    List<Appointment> findAppointmentsByParams(@Param("params") Map<String, Object> params);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Appointment a SET a.isConfirm = :isConfirm WHERE a.id = :id")
    int update(@Param("id") Integer id, @Param("isConfirm") Short isConfirm);
}
