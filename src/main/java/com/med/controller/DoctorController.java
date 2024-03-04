package com.med.controller;

import com.med.model.Doctor;
import com.med.service.DoctorService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

   public ResponseEntity getByDepartmentId (@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(this.doctorService.getByUserId(id), HttpStatus.OK);
    }
}
