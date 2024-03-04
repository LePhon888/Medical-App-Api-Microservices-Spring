package com.med.controller;

import com.med.model.Medicine;
import com.med.model.MedicineUnit;
import com.med.service.MedicineService;
import com.med.service.MedicineUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
