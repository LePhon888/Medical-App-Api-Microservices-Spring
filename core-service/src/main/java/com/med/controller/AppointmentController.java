package com.med.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.med.dto.AppointmentHourDTO;
import com.med.dto.AppointmentPatient;
import com.med.model.*;
import com.med.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

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
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.email}")
    private String emailTopic;

    @Autowired
    private TaskScheduler taskScheduler;


    @PostMapping
    public ResponseEntity create(@RequestBody Map<String, String> appointment) throws ParseException {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z", Locale.US);
        Date date = inputDateFormat.parse(appointment.get("date"));
        Appointment savedAppointment = Appointment.builder()
                .user(userService.getById(Integer.parseInt(appointment.get("userId"))))
                .reason(appointment.get("reason"))
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

    @GetMapping("/hour")
    public List<AppointmentHourDTO> getAppointmentHourByDate(@RequestParam("date") String date, @RequestParam("doctorId") Integer doctorId) {
        return appointmentService.getAppointmentHourByDate(LocalDate.parse(date), doctorId);
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

    @GetMapping("/id/{id}")
    public ResponseEntity getById(@PathVariable Integer id) {
        return new ResponseEntity<>(this.appointmentService.getById(id), HttpStatus.OK);
    }

    @PutMapping("/update-paid")
    public ResponseEntity updateIsPaid(@RequestParam("id") String id,
                                       @RequestParam("date") String paymentTime) {
        try {
            Optional<Appointment> optionalAppointment = Optional.ofNullable(appointmentService.getById(Integer.parseInt(id)));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsedDate = dateFormat.parse(convertToFormattedDateTime(paymentTime));

            if (optionalAppointment.isPresent()) {
                Appointment appointment = optionalAppointment.get();
                appointment.setIsConfirm((short) 1);
                appointment.setIsPaid((short) 1);

                appointment.setPaymentTime(parsedDate);
                Appointment savedAppointment = appointmentService.update(appointment);

                if (savedAppointment != null) {
                    Map<String, String> detailAppointment = new HashMap<>();
                    detailAppointment.put("appointmentId", String.valueOf(savedAppointment.getId()));
                    detailAppointment.put("date", String.valueOf(savedAppointment.getDate()));
                    detailAppointment.put("hour", savedAppointment.getHour().getHour());
                    detailAppointment.put("doctor", savedAppointment.getDoctor().getUser().getLastName() + " " + savedAppointment.getDoctor().getUser().getFirstName());
                    detailAppointment.put("doctorEmail", savedAppointment.getDoctor().getUser().getEmail());
                    detailAppointment.put("reason", savedAppointment.getReason());
                    detailAppointment.put("user", savedAppointment.getUser().getLastName() + " " + savedAppointment.getUser().getFirstName());
                    detailAppointment.put("userEmail", savedAppointment.getUser().getEmail());
                    kafkaTemplate.send(emailTopic, objectMapper.writeValueAsString(detailAppointment));

                    // Schedule the reminder appointment
                    LocalDateTime nextScheduleDateTime = LocalDateTime.of(new java.sql.Date(savedAppointment.getDate().getTime()).toLocalDate(), LocalTime.parse(savedAppointment.getHour().getHour()));
                    // Send 5 minutes before appointment
                    LocalDateTime now = LocalDateTime.now().minusMinutes(5);
                    long delayInSeconds = Duration.between(now, nextScheduleDateTime).getSeconds();
                    taskScheduler.schedule(() -> {
                        try {
                            detailAppointment.put("doctorId", String.valueOf(savedAppointment.getDoctor().getUser().getId()));
                            detailAppointment.put("patientId", String.valueOf(savedAppointment.getUser().getId()));
                            detailAppointment.put("screen", "AppointmentList");
                            kafkaTemplate.send("appointment-topic", objectMapper.writeValueAsString(detailAppointment));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }, Instant.now().plusSeconds(delayInSeconds));

                }

//                this.appointmentService.sendConfirmAppointmentMail(appointment);
                return new ResponseEntity<>(HttpStatus.OK);

            } else {
                return new ResponseEntity<>("Appointment not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

//    @PutMapping("/{id}/is-paid")
//    public ResponseEntity updateIsPaid(@PathVariable Integer id, @RequestParam Short isPaid) {
//        try {
//            Optional<Appointment> optionalAppointment = Optional.ofNullable(appointmentService.getById(id));
//
//            if (optionalAppointment.isPresent()) {
//                Appointment appointment = optionalAppointment.get();
//                appointment.setIsPaid(isPaid);
//
//                Appointment updated = appointmentService.update(appointment);
//
//                if (updated != null) {
//                    if (appointment.getIsPaid() == 1)
//                        this.appointmentService.sendConfirmAppointmentMail(appointment);
//                    return new ResponseEntity<>(HttpStatus.OK);
//                } else {
//                    return new ResponseEntity<>("Failed to update appointment", HttpStatus.INTERNAL_SERVER_ERROR);
//                }
//            }
//        } finally {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }

    @GetMapping("/count")
    public Long countAppointmentsByUserId(@RequestParam("doctorId") String doctorId,
                                          @RequestParam("userId") String userId) {
        return this.appointmentService.countAppointmentsByUserId(userId, doctorId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable("id") Integer id) {
        if (id == null) {
            return ResponseEntity.badRequest().body("Record not found.");
        }

        this.appointmentService.deleteById(id);

        return ResponseEntity.ok("delete success");
    }

    @GetMapping("/off-duty")
    public ResponseEntity getOffDutyScheduleByDoctorId(@RequestParam Map<String, String> params) {
        try {
            Integer doctorId = Integer.valueOf(params.get("doctorId"));
            LocalDate fromDate = null;
            LocalDate toDate = null;

            String fromDateStr = params.get("fromDate");
            String toDateStr = params.get("toDate");

            if (fromDateStr != null && !fromDateStr.isEmpty()) {
                fromDate = LocalDate.parse(fromDateStr);
            }

            if (toDateStr != null && !toDateStr.isEmpty()) {
                toDate = LocalDate.parse(toDateStr);
            }

            return ResponseEntity.ok(appointmentService.getOffDutyScheduleByDoctorId(doctorId, fromDate, toDate));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

    }

    @PostMapping("/off-duty")
    public ResponseEntity createOffDutyAppointment(@RequestBody Map<String, String> payload) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(payload.get("date"));
            Doctor doctor = doctorService.getByUserId(Integer.parseInt(payload.get("doctorId")));
            String[] hourIds = payload.get("hourIds").split("#");

            for (String hourId : hourIds) {
                Appointment savedAppointment = Appointment.builder()
                        .hour(hourService.getById(Integer.parseInt(hourId)))
                        .doctor(doctor)
                        .date(date)
                        .isConfirm((short) 0)
                        .isPaid((short) 0)
                        .build();
                appointmentService.create(savedAppointment);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body("create successfully!");

        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    public static String convertToFormattedDateTime(String input) {
        try {
            // Extract components from the input string
            int year = Integer.parseInt(input.substring(0, 4));
            int month = Integer.parseInt(input.substring(4, 6));
            int day = Integer.parseInt(input.substring(6, 8));
            int hour = Integer.parseInt(input.substring(8, 10));
            int minute = Integer.parseInt(input.substring(10, 12));
            int second = Integer.parseInt(input.substring(12, 14));

            // Format the components into the desired format
            String formattedOutput = String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);

            return formattedOutput;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle parsing or formatting errors as needed
        }
    }
}
