package com.med.emailservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

@Service
public class EmailService {


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.email}")
    private String notificationTopic;
    @KafkaListener(topics = "emailTopic", groupId = "email")
    @Async
    public void sendConfirmAppointmentMail(String appointment) {
        System.out.println("Received Notification for Order - {}" + appointment);
        // send out an email notification
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
    }

}
