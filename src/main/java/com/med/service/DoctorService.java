package com.med.service;

import com.med.model.Doctor;
import com.med.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
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

}
