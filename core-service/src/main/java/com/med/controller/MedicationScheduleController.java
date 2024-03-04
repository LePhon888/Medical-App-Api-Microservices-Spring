package com.med.controller;

import com.med.dto.CreateOrUpdateMedicationScheduleDTO;
import com.med.dto.MedicationScheduleDTO;
import com.med.dto.MedicationScheduleProjection;
import com.med.service.MedicationScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/medication-schedule")
public class MedicationScheduleController {
    @Autowired
    private MedicationScheduleService service;

    @PostMapping("/create")
    public ResponseEntity<String> createMedicationSchedule(@RequestBody CreateOrUpdateMedicationScheduleDTO payload) {
        return this.service.createOrUpdateMedicationSchedule(payload);
    }

    @GetMapping("/user/{id}")
    public List<MedicationScheduleProjection> getMedicationScheduleByUserId(
            @PathVariable(name = "id") Integer userId,
            @RequestParam(name = "isActive") Boolean isActive) {
        return this.service.getMedicationScheduleByUserId(userId, isActive);
    }

    @GetMapping("/{id}")
    public MedicationScheduleDTO getMedicationScheduleById(@PathVariable(name = "id") Integer id) {
        return this.service.getMedicationScheduleById(id);
    }
}
