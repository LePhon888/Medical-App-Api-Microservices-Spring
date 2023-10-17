package com.med.controller;

import com.med.model.*;
import com.med.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public ResponseEntity create(@RequestBody Map<String, Object> appointment) {
        try {
            Appointment savedAppointment = new Appointment();
            Map<String, Object> userObject = (Map<String, Object>) appointment.get("user");
            Map<String, Object> hourObject = (Map<String, Object>) appointment.get("hour");
            int doctorId = (int) appointment.get("doctorId");
//            Map<String, Object> doctorUser = (Map<String, Object>) doctorObject.get("user");
//            Map<String, Object> departmentObject = (Map<String, Object>) doctorObject.get("department");
//            Map<String, Object> feeObject = (Map<String, Object>) appointment.get("fee");
            Map<String, Object> registerUserObject = (Map<String, Object>) appointment.get("registerUser");

            User u = new User();
            u.setFirstName((String) userObject.get("firstName"));
            String dateString = (String) userObject.get("birthday");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateString);
            u.setBirthday(date);
            u.setGender((Integer) userObject.get("gender"));
            u.setEmail((String) userObject.get("email"));
            u.setIsActive((short) 1);
            u.setUserRole("ROLE_PATIENT");
            userService.create(u);


            Doctor doctor = this.doctorService.getById(doctorId);

            Hour hour = this.hourService.getById((Integer) hourObject.get("id"));
            Fee fee = this.feeService.getNew();

            User registerUser = this.userService.getById((Integer) registerUserObject.get("id"));


            savedAppointment.setReason((String) appointment.get("reason"));
//            savedAppointment.setReportImage((String) appointment.get("reportImage"));
            savedAppointment.setDate(dateFormat.parse((String) appointment.get("date")));
            savedAppointment.setIsConfirm((short) 0);
            savedAppointment.setIsPaid((short) 0);
            savedAppointment.setFee(fee);
            savedAppointment.setHour(hour);
            savedAppointment.setDoctor(doctor);
            savedAppointment.setUser(u);
            savedAppointment.setRegisterUser(registerUser);
            appointmentService.create(savedAppointment);
            return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

}

    @GetMapping
    public List<Appointment> getAll() {
        return appointmentService.getAll();
    }

    @GetMapping("/")
    public ResponseEntity<List<Appointment>> getAppointments(@RequestParam Map<String, Object> params) {
        System.out.println(this.appointmentService.getAppointments(params));
        System.out.println((params.get("date")));
        return new ResponseEntity<>(this.appointmentService.getAppointments(params), HttpStatus.OK);
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

                boolean updated = appointmentService.update(appointment);

                if (updated) {
                    if (updated && appointment.getIsConfirm() == 1)
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
}
