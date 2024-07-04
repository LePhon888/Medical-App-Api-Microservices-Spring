package com.med.controller;

import com.med.model.Hour;
import com.med.service.DoctorService;
import com.med.service.HourService;
import jdk.jshell.Snippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/hours")
public class HourController {
    @Autowired
    private HourService hourService;

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public List<Hour> getAll () {
        return this.hourService.getAll();
    }

    @GetMapping("/off-duty")
    public ResponseEntity<?> getOffDutyHoursByDoctorIdAndDate(@RequestParam Map<String, String> params) {
        try {
            String doctorIdStr = params.get("doctorId");
            String dateStr = params.get("date");

            if (doctorIdStr == null || doctorIdStr.isEmpty()) {
                return ResponseEntity.badRequest().body("DoctorId is required.");
            }
            if (dateStr == null || dateStr.isEmpty()) {
                return ResponseEntity.badRequest().body("Date is required.");
            }

            // doctorId here is come from user entity not coming from doctor entity
            Integer doctorId = doctorService.getByUserId(Integer.parseInt(doctorIdStr)).getId();
            LocalDate date = LocalDate.parse(dateStr);

            List<Hour> offDutyHours = this.hourService.getOffDutyHoursByDoctorIdAndDate(doctorId, date);
            return ResponseEntity.ok(offDutyHours);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid doctorId format");
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format");
        }
    }
}
