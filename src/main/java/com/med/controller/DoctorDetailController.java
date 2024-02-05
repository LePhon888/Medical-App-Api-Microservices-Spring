package com.med.controller;

import com.med.dto.DoctorDetailsDTO;
import com.med.service.DoctorDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/doctor-detail")
public class DoctorDetailController {
    @Autowired
    private DoctorDetailService service;

    @GetMapping("/{id}")
    public List<DoctorDetailsDTO> getDoctorDetailsByDoctorId(@PathVariable(value = "id") String doctorId) {
        return this.service.getDoctorDetailsByDoctorId(doctorId);
    }
}
