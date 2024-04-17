package com.med.controller;

import com.med.dto.FeeMinMax;
import com.med.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/fee")
public class FeeController {
    @Autowired
    private FeeService service;

    @GetMapping("/range")
    public FeeMinMax getFeeRange() {
        return this.service.getFeeMinMax();
    }
}
