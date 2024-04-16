package com.med.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.med.dto.MedicationScheduleDetailProjection;
import com.med.dto.MedicationScheduleProjection;
import com.med.service.MedicationScheduleService;
import com.med.service.ScheduleTimeService;
import com.med.utils.Screen;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Service
public class ReminderMedicationService {

    @Autowired
    private ScheduleTimeService scheduleTimeService;

    @Autowired
    private MedicationScheduleService medicationScheduleService;

    @Value("${schedule.enable}")
    private boolean isScheduledEnabled;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kafka.topic.notification}")
    private String notificationTopic;

    @Autowired
    private TaskScheduler taskScheduler;

    private ScheduledFuture<?> scheduledFuture;

    public void notifyScheduleTimeToUser(){
        try {
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
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void initializeScheduler() {
        // Call updateFixedDelay method to initialize scheduling duration
        updateFixedDelay();
    }

    public void updateFixedDelay() {
        List<MedicationScheduleProjection> projectionList = medicationScheduleService.getNextScheduleToReminder();

        // Cancel the current scheduled task if there are no schedules
        if (projectionList.isEmpty()) {
            if (scheduledFuture != null) {
                scheduledFuture.cancel(false);
            }
            return;
        }

        // Cancel the current scheduled task
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }

        // Schedule reminders for each medication schedule
        for (MedicationScheduleProjection projection : projectionList) {
            LocalDateTime nextScheduleDateTime = projection.getDateTime();
            LocalDateTime now = LocalDateTime.now();
            long delayInSeconds = Duration.between(now, nextScheduleDateTime).getSeconds();

            // Schedule the reminder for the specific medication schedule
            scheduledFuture = taskScheduler.schedule(this::notifyScheduleTimeToUser, Instant.now().plusSeconds(delayInSeconds));
        }
    }


}
