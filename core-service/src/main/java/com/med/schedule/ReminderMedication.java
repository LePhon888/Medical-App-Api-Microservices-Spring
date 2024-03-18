package com.med.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.med.dto.MedicationScheduleDetailProjection;
import com.med.service.ScheduleTimeService;
import com.med.utils.Screen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReminderMedication {

    @Autowired
    private ScheduleTimeService scheduleTimeService;

    @Value("${schedule.enable}")
    private boolean isScheduledEnabled;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kafka.topic.notification}")
    private String notificationTopic;

    @Scheduled(fixedRate = 180000) // Set schedule every 3 minutes. Khuong: will consider to increase to 5 minutes.
    public void notifyScheduleTimeToUser() throws JsonProcessingException {
        if (!isScheduledEnabled) {
            return;
        }
        List<MedicationScheduleDetailProjection> list = scheduleTimeService.getScheduleTimeToSendNotification();
        if (!list.isEmpty()) {
            for (MedicationScheduleDetailProjection scheduleDetail : list) {
                // Assuming MedicationReminder is a class representing the reminder details
                Map<String, Object> reminder = new HashMap<>();
                reminder.put("medicineName",scheduleDetail.getMedicineName());
                reminder.put("unitName",scheduleDetail.getUnitName());
                reminder.put("quantity",scheduleDetail.getQuantity());
                reminder.put("userId", scheduleDetail.getId());
                reminder.put("time", scheduleDetail.getTime().format(DateTimeFormatter.ofPattern("hh:mm a")));
                reminder.put("screen", Screen.MedicationBox.getName());
                reminder.put("startDate", LocalDate.now().toString());
                // Send the reminder to the notification-service via Kafka
                kafkaTemplate.send(notificationTopic, objectMapper.writeValueAsString(reminder));
            }
        }
    }
}
