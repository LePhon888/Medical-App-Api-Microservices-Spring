package com.med.repository;

import com.med.dto.DoctorDTO;
import com.med.dto.DoctorDetailDTO;
import com.med.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    List<Doctor> findByDepartmentId(int departmentId);
    @Query("SELECT d FROM Doctor d WHERE d.user.id = :userId")
    Doctor findByUserId(@Param("userId") int userId);
    @Query("""
            SELECT
            new com.med.dto.DoctorDTO(
            d.id, 
            concat(d.user.lastName, ' ', d.user.firstName) ,
            d.user.image, 
            d.department.name, 
            d.hospital,  
            d.hospitalAddress, 
            d.information, 
            d.title,
            d.fee.fee,
            (SELECT ROUND(AVG(r.star),1) FROM Rating r WHERE r.doctor.id = d.id)
            ) 
            FROM Doctor d 
            WHERE (:doctorId IS NULL OR d.id = cast(:doctorId as int))
            AND (:params IS NULL 
                OR (
                    (d.fee.fee BETWEEN :#{#params['feeMin']} AND :#{#params['feeMax']})
                    AND (d.department.name = :#{#params['departmentName']})
                    AND (:#{#params['gender']} = 2 OR d.user.gender = :#{#params['gender']})
                )) 
            """)
    List<DoctorDTO> getDoctorList(@Param("doctorId") String doctorId, @Param("params") Map<String, String> params);

    @Query("""
            SELECT
            new com.med.dto.DoctorDTO(
            d.id, 
            concat(d.user.lastName, ' ', d.user.firstName) ,
            d.user.image, 
            d.department.name, 
            d.hospital,  
            d.hospitalAddress, 
            d.information, 
            d.title,
            d.fee.fee,
            (SELECT ROUND(AVG(r.star),1) FROM Rating r WHERE r.doctor.id = d.id)
            ) 
            FROM Doctor d 
            WHERE (:doctorId IS NULL OR d.id = cast(:doctorId as int))
            """)
    DoctorDTO getDoctorById(@Param("doctorId") String doctorId);

//    d.consultation.label,
//    (SELECT LISTAGG(t.target.label, ',') FROM DoctorTarget t WHERE t.doctor.id = d.id),
}
