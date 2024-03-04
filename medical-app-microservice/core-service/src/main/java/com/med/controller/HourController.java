package com.med.controller;

import com.med.model.Hour;
import com.med.service.HourService;
import jdk.jshell.Snippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/hours")
public class HourController {
    @Autowired
    private HourService hourService;

    @GetMapping
    public List<Hour> getAll () {
        return this.hourService.getAll();
    }
}
