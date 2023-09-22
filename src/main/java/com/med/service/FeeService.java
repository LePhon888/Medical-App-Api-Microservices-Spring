package com.med.service;

import com.med.model.Doctor;
import com.med.model.Fee;
import com.med.repository.DoctorRepository;
import com.med.repository.FeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeeService {
    @Autowired
    private FeeRepository feeRepository;
    public Fee getById (int id) {
        return feeRepository.findById(id).orElse(null);
    }
}
