package com.med.controller;

import com.med.dto.AppointmentPatient;
import com.med.model.*;
import com.med.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);


    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private HourService hourService;

    @Autowired
    private UserService userService;

    @Autowired
    private FeeService feeService;

    @Autowired
    private DepartmentService departmentService;


    @PostMapping
    public ResponseEntity create(@RequestBody Map<String, String> appointment) throws ParseException {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z", Locale.US);
        Date date = inputDateFormat.parse(appointment.get("date"));
        Appointment savedAppointment = Appointment.builder()
                .user(userService.getById(Integer.parseInt(appointment.get("userId"))))
                .reason( appointment.get("reason"))
                .hour(hourService.getById(Integer.parseInt(appointment.get("hour"))))
                .doctor(doctorService.getById(Integer.parseInt(appointment.get("doctorId"))))
                .fee(feeService.getNew())
                .date(date)
                .isConfirm((short) 0)
                .isPaid((short) 0)
                .build();
        appointmentService.create(savedAppointment);
            return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);

    }

    @GetMapping
    public List<Appointment> getAll() {
        return appointmentService.getAll();
    }

    @GetMapping("/doctor/{userId}")
    public ResponseEntity<List<AppointmentPatient>> getAppointments(
            @PathVariable("userId") Integer userId,
            @RequestParam Map<String, Object> params) {
        return new ResponseEntity<>(this.appointmentService.getAppointments(userId, params), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public List<Appointment> getByRegisterUser(@PathVariable Integer id) {
        User u = this.userService.getById(id);
        return appointmentService.getAppointmentsByRegisterUser(u);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity getByUserId(@PathVariable Integer id) {
        User u = this.userService.getById(id);
        return new ResponseEntity<>(this.appointmentService.getByUserId(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/is-confirm")
    public ResponseEntity updateIsConfirm(@PathVariable Integer id, @RequestParam Short isConfirm) {
        try {
            Optional<Appointment> optionalAppointment = Optional.ofNullable(appointmentService.getById(id));

            if (optionalAppointment.isPresent()) {
                Appointment appointment = optionalAppointment.get();
                appointment.setIsConfirm(isConfirm);

                Appointment updated = appointmentService.update(appointment);

                if (updated != null) {
                    if (appointment.getIsConfirm() == 1)
                        this.appointmentService.sendConfirmAppointmentMail(appointment);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Failed to update appointment", HttpStatus.INTERNAL_SERVER_ERROR);
                }



            } else {
                return new ResponseEntity<>("Appointment not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/count")
    public Long countAppointmentsByUserId(@RequestParam("doctorId") String doctorId,
                                          @RequestParam("userId") String userId) {
        return this.appointmentService.countAppointmentsByUserId(userId, doctorId);
    }
}
