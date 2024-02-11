package com.med.service;

import com.med.dto.DoctorDTO;
import com.med.model.Doctor;
import com.med.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    public Doctor getByUserId (int id) {
        return this.doctorRepository.findByUserId(id);
    }
    public Doctor getById (int id) {
        return doctorRepository.findById(id).orElse(null);
    }
    public Doctor create(Doctor doctor) {
        return doctorRepository.save(doctor);
    }
    public List<Doctor> getByDepartmentId (String departmentId) {
        if (departmentId != null && !departmentId.isEmpty())
            return doctorRepository.findByDepartmentId(Integer.parseInt(departmentId));
        else return null;
    }
    public List<DoctorDTO> getDoctorList() {
        return this.doctorRepository.getDoctorList(null);
//        List<Object[]> result = doctorRepository.getDoctorList(null);
//        return result.stream()
//                .map(DoctorDTO::new)
//                .collect(Collectors.toList());
    }

    public DoctorDTO getDoctorById(String id) {
        List<DoctorDTO> list = this.doctorRepository.getDoctorList(id);
        if (list.isEmpty())
             return null;
        return this.doctorRepository.getDoctorList(id).get(0);
    }

}
