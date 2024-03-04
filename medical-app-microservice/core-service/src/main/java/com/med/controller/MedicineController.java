package com.med.controller;

import com.med.model.Medicine;
import com.med.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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
}
