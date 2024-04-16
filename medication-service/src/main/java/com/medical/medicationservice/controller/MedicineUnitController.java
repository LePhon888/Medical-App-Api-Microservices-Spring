package com.medical.medicationservice.controller;


import com.medical.medicationservice.model.MedicineUnit;
import com.medical.medicationservice.service.MedicineUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/medicine-unit")
public class MedicineUnitController {
    @Autowired
    private MedicineUnitService service;

    @GetMapping("/")
    public List<MedicineUnit> getMedicineUnit() {
        return this.service.getMedicineUnit();
    }
}
