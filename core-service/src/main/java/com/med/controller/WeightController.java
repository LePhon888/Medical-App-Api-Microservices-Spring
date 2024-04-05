package com.med.controller;

import com.med.dto.WeightDTO;
import com.med.model.Weight;
import com.med.service.WeightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequestMapping("/api/weight")
@RestController
public class WeightController {
    @Autowired
    private WeightService weightService;

    @GetMapping("/{id}")
    public ResponseEntity<List<WeightDTO>> getWeightsByUserId(@PathVariable(name = "id") Integer id) {
        return new ResponseEntity<>(weightService.getWeightsByUserId(id), HttpStatus.OK);
    }

    @GetMapping("/week/{id}")
    public ResponseEntity<List<WeightDTO>> getWeightsByUserIdWeek(@PathVariable(name = "id") Integer id) {
        return new ResponseEntity<>(weightService.getWeightsByUserIdWeek(id), HttpStatus.OK);
    }

    @GetMapping("/month/{id}")
    public ResponseEntity<List<WeightDTO>> getWeightsByUserIdMonth(@PathVariable(name = "id") Integer id) {
        return new ResponseEntity<>(weightService.getWeightsByUserIdMonth(id), HttpStatus.OK);
    }
}
