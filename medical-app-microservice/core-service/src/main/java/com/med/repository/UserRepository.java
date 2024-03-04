package com.med.repository;

import com.med.model.Appointment;
import com.med.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User getUserByEmail(@Param("email") String email);
    @Query("SELECT u FROM User u WHERE u.verificationCode = :verificationCode")
    public User findByVerificationCode(@Param("verificationCode") String verificationCode);
}
