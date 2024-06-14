package com.med.notificationservice.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.med.notificationservice.dto.NotificationRequest;
import com.med.notificationservice.model.UserDevice;
import com.med.notificationservice.service.NotificationService;
import com.med.notificationservice.service.UserDeviceService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class MedicationReminderListener {

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private NotificationService notificationService;

    @Value("${medicineImage}")
    private String imageUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "#{'${kafka.topic.notification}'}", groupId = "notification-medication-consumer")
    public void consumeMedicationReminder(String reminderJson) {
        try {
            Map<String, Object> reminder = objectMapper.readValue(reminderJson, new TypeReference<>() {});
            UserDevice userDevice = userDeviceService.getUserDeviceByUserId((Integer) reminder.get("userId"));
            if (userDevice != null) {
                Map<String, String> clickActionParams = new HashMap<>();
                clickActionParams.put("screen", reminder.get("screen").toString());
                clickActionParams.put("startDate", (String) reminder.get("startDate"));

                // Create a notification with format
                notificationService.sendNotificationToUser(new NotificationRequest(
                        userDevice.getTokenRegistration(),
                        "Từ hộp thuốc của bạn ",
                        String.format("Đã đến giờ uống %s %s %s vào lúc %s",
                                reminder.get("quantity"),
                                reminder.get("unitName"),
                                reminder.get("medicineName"),
                                reminder.get("time")),
                        imageUrl,
                        (Integer) reminder.get("userId"),
                        clickActionParams));
            }
        } catch (Exception e) {
            System.err.println("Error processing medication reminder: " + e.getMessage());
        }
    }

}
