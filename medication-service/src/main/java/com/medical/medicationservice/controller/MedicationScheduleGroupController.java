package com.medical.medicationservice.controller;

import com.medical.medicationservice.dto.CreateMedicationScheduleGroup;
import com.medical.medicationservice.schedule.ReminderMedicationService;
import com.medical.medicationservice.service.MedicationScheduleGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/medication-schedule-group")
public class MedicationScheduleGroupController {

    @Autowired
    private MedicationScheduleGroupService service;
    @Autowired
    private ReminderMedicationService reminderMedicationService;


    @GetMapping("/{id}")
    public CreateMedicationScheduleGroup getMedicationScheduleGroupById(@PathVariable("id") Integer id) {
        return service.getById(id);
    }

    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createMedicationScheduleGroup(@RequestBody CreateMedicationScheduleGroup payload) {
        ResponseEntity<?> response = this.service.createOrUpdate(payload);
        reminderMedicationService.rescheduleReminderNotification();
        return response;
    }


}
