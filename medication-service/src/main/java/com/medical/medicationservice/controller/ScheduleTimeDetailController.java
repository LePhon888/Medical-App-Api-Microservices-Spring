package com.medical.medicationservice.controller;


import com.medical.medicationservice.dto.HistoryMedication;
import com.medical.medicationservice.dto.ScheduleTimeDetailDTO;
import com.medical.medicationservice.service.ScheduleTimeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/schedule-time-detail")
public class ScheduleTimeDetailController {
    @Autowired
    private ScheduleTimeDetailService service;

    @PostMapping("/createOrUpdate")
    public ResponseEntity<String> createOrUpdate(@RequestBody ScheduleTimeDetailDTO detail) {
        return this.service.createOrUpdate(detail);
    }

    @GetMapping("/user/{userId}")
    public Page<HistoryMedication> getHistoryMedicationByUserId(@PathVariable Integer userId, Pageable pageable) {
        return this.service.getHistoryMedicationByUserId(userId, pageable);
    }

}
