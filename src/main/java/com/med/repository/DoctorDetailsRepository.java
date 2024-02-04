package com.med.repository;

import com.med.model.DoctorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorDetailsRepository extends JpaRepository<DoctorDetails, Integer> {
    @Query("SELECT " +
            "d.category, d.title, d.place, d.fromDate, d.toDate " +
            "FROM DoctorDetails d " +
            "WHERE d.doctor.id = cast(:doctorId as int) " +
            "ORDER BY d.category, d.fromDate DESC ")
    List<Object[]> getDoctorDetailsByDoctorId(String doctorId);
}
