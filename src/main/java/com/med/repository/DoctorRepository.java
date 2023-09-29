package com.med.repository;

import com.med.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    List<Doctor> findByDepartmentId(int departmentId);
}
