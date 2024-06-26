package com.medical.medicationservice.controller;



import com.medical.medicationservice.dto.MedicationScheduleDetailProjection;
import com.medical.medicationservice.dto.ScheduleTimeDTO;
import com.medical.medicationservice.service.ScheduleTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/schedule-time")
public class ScheduleTimeController {
    @Autowired
    private ScheduleTimeService service;

    @GetMapping("/user/{userId}")
    public List<MedicationScheduleDetailProjection> getScheduleTime(@RequestParam LocalDate startDate,
                                                                    @PathVariable(name = "userId") Integer userId) {
        return this.service.getScheduleTime(userId, startDate);
    }

    @GetMapping("/medication-schedule/{id}")
    public List<ScheduleTimeDTO> getScheduleTimeByMedicationScheduleId(@PathVariable(name = "id") Integer id) {
        return this.service.getScheduleTimeByMedicationScheduleId(id);
    }

}
