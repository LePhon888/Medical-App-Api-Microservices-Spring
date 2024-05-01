package com.medical.medicationservice.controller;

import com.medical.medicationservice.dto.CreateMedicationScheduleDTO;
import com.medical.medicationservice.dto.MedicationScheduleDTO;
import com.medical.medicationservice.dto.MedicationScheduleProjection;
import com.medical.medicationservice.schedule.ReminderMedicationService;
import com.medical.medicationservice.service.MedicationScheduleService;
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
    @Autowired
    private ReminderMedicationService reminderMedicationService;

    @PostMapping("/createOrUpdate")
    public ResponseEntity<String> createMedicationSchedule(@RequestBody CreateMedicationScheduleDTO payload) {
        ResponseEntity<String> response = this.service.createOrUpdateMedicationSchedule(payload);
        reminderMedicationService.rescheduleReminderNotification();
        return response;
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

    @DeleteMapping("/{id}")
    public void deleteMedicationScheduleById(@PathVariable(name = "id") Integer id) {
        this.service.deleteMedicationScheduleById(id);
    }
}
