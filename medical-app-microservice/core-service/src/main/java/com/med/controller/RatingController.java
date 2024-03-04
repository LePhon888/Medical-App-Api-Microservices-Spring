package com.med.controller;

import com.med.dto.RatingDTO;
import com.med.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/stats/{id}")
    public List<Object[]> getRatingStatsByDoctorId(@PathVariable("id") String id) {
        return serivce.getRatingStatsByDoctorId(id);
    }

    @PostMapping
    public String createRating(@RequestBody Map<String, String> Payload) {
        return this.serivce.createOrUpdateRating(Payload);
    }
}
