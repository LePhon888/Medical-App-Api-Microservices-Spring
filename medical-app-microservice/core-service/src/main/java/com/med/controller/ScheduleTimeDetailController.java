package com.med.controller;


import com.med.dto.ScheduleTimeDetailDTO;
import com.med.service.ScheduleTimeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
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

}
