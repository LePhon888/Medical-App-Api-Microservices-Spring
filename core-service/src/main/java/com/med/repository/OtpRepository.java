package com.med.repository;

import com.med.model.Otp;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Integer> {

    @Query("""
        SELECT 
            otp
        FROM 
            Otp otp
        WHERE 
            otp.code = :code 
            AND otp.email = :email 
            AND otp.expiredAt > :dateTime
        ORDER BY 
            otp.expiredAt DESC
    """)
    Optional<Otp> findValidOtpByEmailAndCode(String email, Integer code, LocalDateTime dateTime);

    @Modifying
    @Query("""
        DELETE FROM Otp WHERE email = :email
    """)
    void deleteAllOtpByEmail(String email);
}
