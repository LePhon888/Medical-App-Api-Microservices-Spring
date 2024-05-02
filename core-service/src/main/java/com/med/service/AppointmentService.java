package com.med.service;

import com.med.dto.AppointmentHourDTO;
import com.med.dto.AppointmentPatient;
import com.med.model.Appointment;
import com.med.model.User;
import com.med.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public Appointment create(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    public List<AppointmentPatient> getAppointments(Integer userId, Map<String, Object> params) {
        if (params.get("startDate") != null) {
            try {
                params.put("startDate", LocalDate.parse(params.get("startDate").toString()));
            } catch (DateTimeParseException e) {
                params.put("startDate", null);
            }
        }
        if (params.get("endDate") != null) {
            try {
                params.put("endDate", LocalDate.parse(params.get("endDate").toString()));
            } catch (DateTimeParseException e) {
                params.put("endDate", null);
            }
        }
        return appointmentRepository.findAppointmentsByParams(userId, params);
    }


    public Appointment update(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

//    @Async
//    public void sendConfirmAppointmentMail(Appointment appointment) {
//        String code = UUID.randomUUID().toString().substring(0, 6);
//        String doctorName = appointment.getDoctor().getUser().getLastName() + " " + appointment.getDoctor().getUser().getFirstName();
//        String patientName = appointment.getUser().getLastName() + " " + appointment.getUser().getFirstName() ;
//        String date = appointment.getDate().toString();
//        String time = appointment.getHour().getHour();
//
//        SimpleMailMessage patientMessage = new SimpleMailMessage();
//        patientMessage.setTo(appointment.getUser().getEmail());
//        patientMessage.setSubject("Xác Nhận Lịch Hẹn và Mã truy cập phòng gọi Video/Chat");
//        patientMessage.setText(
//                "Chào  " + patientName + "\n\n" +
//                        "Chúng tôi rất vui thông báo xác nhận lịch hẹn của bạn với bác sĩ " + doctorName + " vào ngày " + date + " vào lúc " + time + ".\n\n" +
//                        "Chi Tiết Lịch Hẹn:\n" +
//                        "- Ngày: " + appointment.getDate().toString() + "\n" +
//                        "- Thời Gian: " + time + "\n" +
//                        "- Bác Sĩ: " + doctorName
//                        + "\n\n" +
//                        "Để chuẩn bị cho cuộc hẹn của bạn, vui lòng đảm bảo bạn đã có bất kỳ hồ sơ y tế cần thiết hoặc câu hỏi mà bạn muốn thảo luận với bác sĩ.\n\n" +
//                        "Mã truy cập phòng gọi Video/Chat: " + code + "\n\n" +
//                        "Để tham gia vào phòng chat và bắt đầu cuộc tư vấn với bác sĩ, đơn giản là nhập mã truy cập được cung cấp vào nền tảng của chúng tôi vào thời gian đã lên lịch. \n" +
//                        "Nếu bạn gặp bất kỳ khó khăn về mặt kỹ thuật nào, đội ngũ hỗ trợ của chúng tôi sẽ luôn sẵn sàng giúp đỡ.\n\n" +
//                        "Chúng tôi rất mong được hỗ trợ bạn với các nhu cầu chăm sóc sức khỏe của bạn.\n\n" +
//                        "Trân trọng,\n\n" +
//                        "Medcare\n" +
//                        "Luôn vì bạn\n");
//
//        mailSender.send(patientMessage);
//
//        SimpleMailMessage doctorMessage = new SimpleMailMessage();
//        doctorMessage.setTo(appointment.getDoctor().getUser().getEmail());
//        doctorMessage.setSubject("Thông Báo Cuộc Hẹn Mới");
//        doctorMessage.setText(
//                "Chào bác sĩ " + appointment.getDoctor().getUser().getLastName() + " " + appointment.getDoctor().getUser().getFirstName() + ",\n\n" +
//                        "Bạn có một cuộc hẹn mới với bệnh nhân " + patientName + " vào ngày " + date + " lúc " + time + ".\n\n" +
//                        "Chi Tiết Cuộc Hẹn:\n" +
//                        "- Ngày: " + date + "\n" +
//                        "- Thời Gian: " + time + "\n" +
//                        "- Bệnh Nhân: " + patientName + "\n\n" +
//                        "Mã truy cập phòng gọi Video/Chat: " + code + "\n\n" +
//                        "Để tham gia vào phòng gọi Video hoặc Chat bạn chỉ nhập mã truy cập này trên ứng dụng của chúng tôi vào thời gian đã lên lịch.\n" +
//                        "Nếu bạn gặp bất kỳ khó khăn kỹ thuật nào, đội ngũ hỗ trợ của chúng tôi luôn sẵn sàng giúp đỡ.\n\n" +
//                        "Trân trọng,\n\n" +
//                        "Medcare\n" +
//                        "Luôn vì bạn\n");
//        mailSender.send(doctorMessage);
//    }

    public Appointment getById(int id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    public List<Appointment> getAppointmentsByRegisterUser(User u) {
        return appointmentRepository.findByRegisterUser(u);
    }

    public List<Appointment> getByUserId(int id) {
        return appointmentRepository.findByUserId(id);
    }

    public Long countAppointmentsByUserId(String userId, String doctorId) {
        if (userId.isEmpty() || doctorId.isEmpty()) {
            return (long) -1;
        }
        return this.appointmentRepository.countAppointmentsByUserId(userId, doctorId);
    }

    public List<AppointmentHourDTO> getAppointmentHourByDate(LocalDate date, Integer doctorId) {
        return appointmentRepository.getAppointmentHourByDate(date, doctorId);
    }
}
