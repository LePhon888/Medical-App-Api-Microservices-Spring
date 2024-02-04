package com.med.controller;

import com.med.dto.DoctorDTO;
import com.med.dto.DoctorDetailsDTO;
import com.med.model.Doctor;
import com.med.model.DoctorDetails;
import com.med.service.DoctorDetailsService;
import com.med.service.DoctorService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/doctor-details")
public class DoctorDetailsController {
    @Autowired
    private DoctorDetailsService service;

    @GetMapping("/{id}")
    public List<DoctorDetailsDTO> getDoctorDetailsByDoctorId(@PathVariable(value = "id") String doctorId) {
        return this.service.getDoctorDetailsByDoctorId(doctorId);
    }
}
