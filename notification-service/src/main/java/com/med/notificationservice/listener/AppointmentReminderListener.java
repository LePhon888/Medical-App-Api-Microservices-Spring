package com.med.notificationservice.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.med.notificationservice.dto.NotificationRequest;
import com.med.notificationservice.model.UserDevice;
import com.med.notificationservice.service.NotificationService;
import com.med.notificationservice.service.UserDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AppointmentReminderListener {

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private NotificationService notificationService;

    @Value("${appointmentImage}")
    private String imageUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "appointment-topic", groupId = "notification-appointment-consumer")
    public void appointmentReminder(String reminderJson) {
        try {
            Map<String, String> reminder = objectMapper.readValue(reminderJson, new TypeReference<>() {});

            UserDevice patientDevice = userDeviceService.getUserDeviceByUserId(Integer.valueOf(reminder.get("patientId")));
            UserDevice doctorDevice = userDeviceService.getUserDeviceByUserId(Integer.valueOf(reminder.get("doctorId")));

            if (patientDevice != null) {
                Map<String, String> clickActionParams = new HashMap<>();
                clickActionParams.put("screen", reminder.get("screen").toString());
                // Create a notification with format
                notificationService.sendNotificationToUser(new NotificationRequest(
                        patientDevice.getTokenRegistration(),
                        "Nhắc nhở lịch khám ",
                        String.format("Bạn có lịch khám với bác sĩ %s vào lúc %s",
                                reminder.get("doctor"),
                                reminder.get("hour")),
                        imageUrl,
                        Integer.valueOf(reminder.get("patientId")),
                        clickActionParams));
            }

            if (doctorDevice != null) {
                Map<String, String> clickActionParams = new HashMap<>();
                clickActionParams.put("screen", reminder.get("screen").toString());
                // Create a notification with format
                notificationService.sendNotificationToUser(new NotificationRequest(
                        patientDevice.getTokenRegistration(),
                        "Nhắc nhở lịch khám ",
                        String.format("Bạn có lịch khám với bệnh nhận %s vào lúc %s",
                                reminder.get("user"),
                                reminder.get("hour")),
                        imageUrl,
                        Integer.valueOf(reminder.get("doctorId")),
                        clickActionParams));
            }
        } catch (Exception e) {
            System.err.println("Error processing appointment reminder: " + e.getMessage());
        }
    }

}
