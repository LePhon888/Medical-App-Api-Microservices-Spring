package com.med.notificationservice.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class EmailService {


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private JavaMailSender mailSender;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Value("${kafka.topic.email}")
    private String notificationTopic;

    @KafkaListener(topics = "emailTopic", groupId = "email")
    public void sendConfirmAppointmentMail(String appointment) throws JsonProcessingException {
        Map<String, String> map = objectMapper.readValue(appointment, Map.class);
        String code = UUID.randomUUID().toString().substring(0, 6);

        SimpleMailMessage patientMessage = new SimpleMailMessage();
        patientMessage.setTo(map.get("userEmail"));
        patientMessage.setSubject("Xác Nhận Lịch Hẹn và Mã truy cập phòng gọi Video/Chat");

        patientMessage.setText(templatePatient(map.get("user"), map.get("doctor"), map.get("date"), map.get("hour"), code));
        SimpleMailMessage doctorMessage = new SimpleMailMessage();
        doctorMessage.setTo(map.get("doctorEmail"));
        doctorMessage.setSubject("Thông Báo Cuộc Hẹn Mới");
        doctorMessage.setText(templateDoctor(map.get("user"), map.get("doctor"), map.get("date"), map.get("hour"), code));
        try {
            mailSender.send(patientMessage);
            mailSender.send(doctorMessage);
        } catch (MailException e) {
            log.error("Could not send e-mail", e);
        }

    }

    @KafkaListener(topics = "verifyEmail", groupId = "email")
    public void sendVerifyEmail(String email) throws JsonProcessingException {
        Map<String, String> map = objectMapper.readValue(email, Map.class);
        System.out.println("VerifyVerifyVerify " + email);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(map.get("email"));
        message.setSubject("Xác minh tài Khoản Medcare");
        message.setText(templateVerifyEmail(map.get("name"), Integer.parseInt(map.get("code"))));
        try {
            mailSender.send(message);
        } catch (MailException e) {
            log.error("Could not send e-mail", e);
        }
    }

    public String templateDoctor(String user, String doctor, String date, String hour, String code) {
        String template = "Chào bác sĩ " + doctor + "\n\n" +
                "Bạn có một cuộc hẹn mới với bệnh nhân " + user + " vào ngày " + date + " lúc " + hour + ".\n\n" +
                "Chi Tiết Cuộc Hẹn:\n" +
                "- Ngày: " + date + "\n" +
                "- Thời Gian: " + hour + "\n" +
                "- Bệnh Nhân: " + user + "\n\n" +
                "Mã truy cập phòng gọi Video/Chat: " + code + "\n\n" +
                "Để tham gia vào phòng gọi Video hoặc Chat bạn chỉ nhập mã truy cập này trên ứng dụng của chúng tôi vào thời gian đã lên lịch.\n" +
                "Nếu bạn gặp bất kỳ khó khăn kỹ thuật nào, đội ngũ hỗ trợ của chúng tôi luôn sẵn sàng giúp đỡ.\n\n" +
                "Trân trọng,\n\n" +
                "Medcare\n" +
                "Luôn vì bạn\n";
        return template;
    }

    public String templatePatient(String user, String doctor, String date, String hour, String code) {
        String template = "Chào " + user + "\n\n" +
                "Chúng tôi rất vui thông báo xác nhận lịch hẹn của bạn với bác sĩ " + doctor + " vào ngày " + date + " vào lúc " + hour + ".\n\n" +
                "Chi Tiết Lịch Hẹn:\n" +
                "- Ngày: " + date + "\n" +
                "- Thời Gian: " + hour + "\n" +
                "- Bác Sĩ: " + doctor + "\n\n" +
                "Để chuẩn bị cho cuộc hẹn của bạn, vui lòng đảm bảo bạn đã có bất kỳ hồ sơ y tế cần thiết hoặc câu hỏi mà bạn muốn thảo luận với bác sĩ.\n\n" +
                "Mã truy cập phòng gọi Video/Chat: " + code + "\n\n" +
                "Để tham gia vào phòng chat và bắt đầu cuộc tư vấn với bác sĩ, đơn giản là nhập mã truy cập được cung cấp vào nền tảng của chúng tôi vào thời gian đã lên lịch. \n" +
                "Nếu bạn gặp bất kỳ khó khăn về mặt kỹ thuật nào, đội ngũ hỗ trợ của chúng tôi sẽ luôn sẵn sàng giúp đỡ.\n\n" +
                "Chúng tôi rất mong được hỗ trợ bạn với các nhu cầu chăm sóc sức khỏe của bạn.\n\n" +
                "Trân trọng,\n\n" +
                "Medcare\n" +
                "Luôn vì bạn\n";
        return template;
    }

    public String templateVerifyEmail(String name, int code) {
        String content = "Gửi "+ name + ",\n" +
                "Xác minh tài khoản của bạn \n" +
                "Mã xác minh của bạn là: " + code + "\n" +
                "Mã xác minh sẽ có hiệu lực trong 30 phút. Vui lòng không chia sẻ mã này cho người khác \n" +
                "Medcare.";
        return content;
    }

}
