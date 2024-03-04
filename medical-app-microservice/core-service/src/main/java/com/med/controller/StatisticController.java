package com.med.controller;

import com.med.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class StatisticController {
    @Autowired
    private StatisticService service;

    @GetMapping("/stats-patient-visits/")
    public ResponseEntity getPatientVisits(@RequestParam Map<String, Object> params) {
        return new ResponseEntity<>(this.service.countFrequentPatientVisitsByTime(params), HttpStatus.OK);
    }

    @GetMapping("/stats-revenue/")
    public ResponseEntity getRevenue(@RequestParam Map<String, Object> params) {
        return new ResponseEntity<>(this.service.getRevenueByTime(params), HttpStatus.OK);
    }

}
