package com.med.controller;

import com.med.dto.RatingDTO;
import com.med.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/rating")
public class RatingController {
    @Autowired
    private RatingService serivce;

    @GetMapping("/{id}")
    public List<RatingDTO> getRatingByDoctorId(@PathVariable("id") String id) {
        return serivce.getRatingByDoctorId(id);
    }

}
