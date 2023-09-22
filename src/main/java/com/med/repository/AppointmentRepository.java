package com.med.repository;

import com.med.model.Appointment;
import com.med.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Query("SELECT a FROM Appointment a WHERE a.registerUser = :registerUser")
    List<Appointment> findByRegisterUser(@Param("registerUser") User registerUser);}
