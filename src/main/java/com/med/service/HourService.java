package com.med.service;

import com.med.model.Doctor;
import com.med.model.Hour;
import com.med.repository.DoctorRepository;
import com.med.repository.HourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HourService {
    @Autowired
    private HourRepository hourRepository;
    public Hour getById (int id) {
        return hourRepository.findById(id).orElse(null);
    }
    public List<Hour> getAll () {
        return hourRepository.findAll();
    }

}
