package com.med.repository;

import com.med.dto.DoctorDetailDTO;
import com.med.model.DoctorDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorDetailRepository extends JpaRepository<DoctorDetail, Integer> {
    @Query("SELECT new com.med.dto.DoctorDetailDTO(" +
            "d.category, d.title, d.place, d.fromDate, d.toDate)" +
            "FROM DoctorDetail d " +
            "WHERE d.doctor.id = cast(:doctorId as int) " +
            "ORDER BY d.category, d.fromDate DESC ")
    List<DoctorDetailDTO> getDoctorDetailsByDoctorId(String doctorId);
}
