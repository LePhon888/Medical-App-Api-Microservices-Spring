package com.medical.medicationservice.controller;


import com.medical.medicationservice.model.Medicine;
import com.medical.medicationservice.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/medicine")
public class MedicineController {
    @Autowired
    private MedicineService service;

    @GetMapping("/")
    public Page<Medicine> getMedicine(@RequestParam Map<String, String> params) {
        return this.service.getMedicines(params);
    }

    @GetMapping("/all")
    public List<Medicine> getMedicineAll() {
        return this.service.getAllMedicines();
    }

}
