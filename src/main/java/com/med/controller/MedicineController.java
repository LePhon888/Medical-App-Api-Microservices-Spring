package com.med.controller;

import com.med.model.Medicine;
import com.med.model.Post;
import com.med.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/medicine")
public class MedicineController {
    @Autowired
    private MedicineService medicineService;
    @GetMapping
    public List<Medicine> getAll () {
        return this.medicineService.getAllMedicines();
    }
}
