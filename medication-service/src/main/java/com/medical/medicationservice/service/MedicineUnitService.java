package com.medical.medicationservice.service;


import com.medical.medicationservice.model.MedicineUnit;
import com.medical.medicationservice.repository.MedicineUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicineUnitService {
    @Autowired
    private MedicineUnitRepository repository;

    public List<MedicineUnit> getMedicineUnit () {
        return this.repository.findAll();
    }
}
