package com.medical.medicationservice.controller;

import com.medical.medicationservice.dto.MedicationScheduleGroupDTO;
import com.medical.medicationservice.schedule.ReminderMedicationService;
import com.medical.medicationservice.service.MedicationScheduleGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public MedicationScheduleGroupDTO getMedicationScheduleGroupById(@PathVariable("id") Integer id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteMedicationScheduleGroupById(@PathVariable("id") Integer id) {
        service.deleteById(id);
    }

    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createMedicationScheduleGroup(@RequestBody MedicationScheduleGroupDTO payload) {
        ResponseEntity<?> response = this.service.createOrUpdate(payload);
        if (response.getStatusCode() == HttpStatus.OK) {
            reminderMedicationService.rescheduleReminderNotification();
        }
        return response;
    }


}
