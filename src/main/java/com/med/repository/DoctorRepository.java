package com.med.repository;

import com.med.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    List<Doctor> findByDepartmentId(int departmentId);
    @Query("SELECT d FROM Doctor d WHERE d.user.id = :userId")
    Doctor findByUserId(@Param("userId") int userId);

}
