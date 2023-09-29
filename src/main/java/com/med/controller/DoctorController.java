package com.med.controller;

import com.med.model.Doctor;
import com.med.service.DoctorService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @GetMapping("/department")
    public List<Doctor> getByDepartmentId (@PathParam(value = "departmentId") String departmentId) {
        return this.doctorService.getByDepartmentId(departmentId);
    }
}
