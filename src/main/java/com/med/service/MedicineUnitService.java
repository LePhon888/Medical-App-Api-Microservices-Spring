package com.med.service;

import com.med.model.MedicineUnit;
import com.med.repository.MedicineUnitRepository;
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
