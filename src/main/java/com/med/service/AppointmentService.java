package com.med.service;

import com.med.model.Appointment;
import com.med.model.User;
import com.med.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public Appointment create(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }
    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getAppointments(Map<String, Object> params) {
        if (params != null) {
            String userId = params.get("userId").toString();
            if (userId != null && !userId.isEmpty()) {
                params.put("doctorId", doctorService.getByUserId(Integer.parseInt(userId)).getId());
            }
        }
        return appointmentRepository.findAppointmentsByParams(params);
    }

    public boolean update(Appointment appointment) {
        boolean result = appointmentRepository.update(appointment.getId(), appointment.getIsConfirm()) > 0;
        return result;
    }

    @Async
    public void sendConfirmAppointmentMail(Appointment appointment) {

        String code = UUID.randomUUID().toString().substring(0, 6);
        String doctorName = appointment.getDoctor().getUser().getFirstName() + " " + appointment.getDoctor().getUser().getLastName();
        String date = appointment.getDate().toString();
        String time =  appointment.getHour().getHour();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(appointment.getUser().getEmail());
        message.setSubject("Xác Nhận Lịch Hẹn và Mã Truy Cập Phòng Chat");
        message.setText(
                "Kính gửi Quý khách thân mến,\n\n" +
                "Chúng tôi rất vui thông báo xác nhận lịch hẹn của bạn với bác sĩ " + doctorName +" vào ngày "+ date +" vào lúc "+ time +".\n\n" +
                "Chi Tiết Lịch Hẹn:\n" +
                "- Ngày: " + appointment.getDate().toString() + "\n" +
                "- Thời Gian: "+ time +"\n" +
                "- Bác Sĩ: " +  doctorName
                +"\n\n" +
                "Để chuẩn bị cho cuộc hẹn của bạn, vui lòng đảm bảo bạn đã có bất kỳ hồ sơ y tế cần thiết hoặc câu hỏi mà bạn muốn thảo luận với bác sĩ.\n\n" +
                "Mã Truy Cập Phòng Chat: "+ code +"\n\n" +
                "Để tham gia vào phòng chat và bắt đầu cuộc tư vấn với bác sĩ, đơn giản là nhập mã truy cập được cung cấp vào nền tảng của chúng tôi vào thời gian đã lên lịch. \n" +
                 "Nếu bạn gặp bất kỳ khó khăn về mặt kỹ thuật nào, đội ngũ hỗ trợ của chúng tôi sẽ luôn sẵn sàng giúp đỡ.\n\n" +
                "Nếu bạn có bất kỳ câu hỏi hoặc cần phải thay đổi lịch hẹn, xin vui lòng liên hệ với phòng khám của chúng tôi theo email lephon888@gmail.com.\n\n" +
                "Chúng tôi rất mong được hỗ trợ bạn với các nhu cầu chăm sóc sức khỏe của bạn.\n\n" +
                "Trân trọng,\n\n" +
                "Medical App\n" +
                "Thông tin liên hệ: lephon888@gmail.com");
        mailSender.send(message);
    }

    public Appointment getById(int id) {
        return appointmentRepository.findById(id).orElse(null);
    }
    public List<Appointment> getAppointmentsByRegisterUser(User u) {
        return appointmentRepository.findByRegisterUser(u);
    }
}
