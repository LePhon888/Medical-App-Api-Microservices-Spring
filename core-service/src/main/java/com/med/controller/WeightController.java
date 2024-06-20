package com.med.controller;

import com.med.dto.WeightDTO;
import com.med.model.Weight;
import com.med.service.UserService;
import com.med.service.WeightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Period;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RequestMapping("/api/weight")
@RestController
public class WeightController {
    @Autowired
    private WeightService weightService;

    @Autowired
    private UserService userService;

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


    @GetMapping("/year/{id}")
    public ResponseEntity<List<WeightDTO>> getWeightsByUserIdYear(@PathVariable(name = "id") Integer id) {
        return new ResponseEntity<>(weightService.getWeightsByUserIdYear(id), HttpStatus.OK);
    }

    @GetMapping("/new/{id}")
    public ResponseEntity<List<WeightDTO>> getNewWeight(@PathVariable(name = "id") Integer id) {
        return new ResponseEntity<>(weightService.getNewWeight(id), HttpStatus.OK);
    }

    @GetMapping("/average/{id}")
    public ResponseEntity<List<Map<String, Double>>> averageWeight(@PathVariable(name = "id") Integer id) {
        return new ResponseEntity<>(weightService.averageWeight(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<WeightDTO> saveWeight(@RequestBody Map<String, Integer> weight) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        String classification = "";
        float doubleWeight = ((float) weight.get("height") / 100) * ((float) weight.get("height") / 100);
        float bmi = (float) weight.get("number") / doubleWeight;
        float bmr = 0;


        Date d = new Date();
        Date localBirthDate = userService.getById(weight.get("userId")).getBirthday();
        Long userAge = (d.getTime() - localBirthDate.getTime()) / (1000L * 60 * 60 * 24 * 365);

        //0 is male, 1 is female
        //get old base birthday user
        if (userService.getById(weight.get("userId")).getGender() == 0) {
            bmr = 66 + (13.75f * weight.get("number")) + (5 * weight.get("height")) - (6.8f * userAge);
        } else {
            bmr = 655 + (9.6f * weight.get("number")) + (1.8f * weight.get("height")) - (4.7f * userAge);
        }
        if (bmi < 18.5) {
            classification = "Nhẹ cân";
        } else if (bmi >= 18.5 && bmi <= 22.99999) {
            classification = "Cân nặng bình thường";
        } else if (bmi >= 23 && bmi <= 24.99999) {
            classification = "Thừa cân";
        } else if (bmi >= 25 && bmi <= 29.99999) {
            classification = "Béo phì độ I";
        } else if (bmi >= 30) {
            classification = "Béo phì độ II";
        }
        Weight newWeight = Weight.builder()
                .number(Float.valueOf(weight.get("number")))
                .date(currentDate)
                .user(userService.getById(Integer.parseInt(String.valueOf(weight.get("userId")))))
                .time(currentTime)
                .height(Float.valueOf(weight.get("height")))
                .bmi((float) (Math.ceil(bmi * 100) / 100))
                .bmr((float) (Math.ceil(bmr * 100) / 100))
                .classification(classification)
                .build();
        return new ResponseEntity<>(weightService.saveWeight(newWeight), HttpStatus.OK);
    }

    @PostMapping("/child")
    public ResponseEntity<WeightDTO> saveWeightChild(@RequestBody Map<String, Integer> weight) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        Weight newWeight = Weight.builder()
                .number(Float.valueOf(weight.get("number")))
                .date(currentDate)
                .user(userService.getById(Integer.parseInt(String.valueOf(weight.get("userId")))))
                .time(currentTime)
                .height(Float.valueOf(weight.get("height")))
                .build();
        return new ResponseEntity<>(weightService.saveWeight(newWeight), HttpStatus.OK);
    }
}
