package com.medical.medicationservice.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medical.medicationservice.dto.MedicationScheduleDetailProjection;
import com.medical.medicationservice.dto.MedicationScheduleProjection;
import com.medical.medicationservice.repository.ScheduleTimeDetailRepository;
import com.medical.medicationservice.service.MedicationScheduleService;
import com.medical.medicationservice.service.ScheduleTimeService;
import com.medical.medicationservice.utils.Screen;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

    @Autowired
    private ScheduleTimeDetailRepository scheduleTimeDetailRepository;

    @Value("${schedule.enable}")
    private boolean isScheduledEnabled;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kafka.topic.notification}")
    private String notificationTopic;

    @Autowired
    private TaskScheduler taskScheduler;

    private ScheduledFuture<?> scheduledReminder;

    @PostConstruct
    public void initializeScheduler() {
        this.rescheduleReminderNotification();
    }

    @Scheduled(cron = "0 0 0 * * ?",  zone = "Asia/Bangkok") // Reschedule after 12AM Daily
    public void rescheduleNotifyMedicationSchedule() {
        this.rescheduleReminderNotification();
    }

    public void notifyScheduleTimeToUser(){
        if (!isScheduledEnabled) {
            return;
        }
        List<MedicationScheduleDetailProjection> list = scheduleTimeService.getScheduleTimeToSendNotification();
        if (!list.isEmpty()) {
            for (MedicationScheduleDetailProjection scheduleDetail : list) {
                handleNotifyWrapper(scheduleDetail);
            }
        }
    }

    private void handleNotifyWrapper(MedicationScheduleDetailProjection scheduleDetail) {
        try {
            // Assuming MedicationReminder is a class representing the reminder details
            Map<String, Object> reminder = new HashMap<>();
            reminder.put("medicineName",scheduleDetail.getMedicineName());
            reminder.put("unitName",scheduleDetail.getUnitName());
            reminder.put("quantity",scheduleDetail.getQuantity());
            reminder.put("userId", scheduleDetail.getUserId());
            reminder.put("time", scheduleDetail.getTime().format(DateTimeFormatter.ofPattern("hh:mm a")));
            reminder.put("screen", Screen.MedicationBox.getName());
            reminder.put("startDate", LocalDate.now().toString());
            // Send the reminder to the notification-service via Kafka
            kafkaTemplate.send(notificationTopic, objectMapper.writeValueAsString(reminder));
            // Send check medication usage after 5 minutes
            taskScheduler.schedule(() -> checkMedicationUsage(scheduleDetail), Instant.now().plus(5, ChronoUnit.MINUTES));
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }

    private void checkMedicationUsage(MedicationScheduleDetailProjection scheduleDetail) {

        // Ignore the check usage after new date
        if (LocalDate.now().isAfter(scheduleDetail.getDate())) {
            return;
        }

        Boolean checkUsage = scheduleTimeDetailRepository.checkMedicationInteraction(
                scheduleDetail.getId(),
                scheduleDetail.getScheduleTimeId(),
                scheduleDetail.getDate()
        );

        // The user haven't interacted yet, so keep send notification
        if (checkUsage == null) {
            handleNotifyWrapper(scheduleDetail);
        }
    }

    public void rescheduleReminderNotification() {
        List<MedicationScheduleProjection> projectionList = medicationScheduleService.getNextScheduleToReminder();

        // Cancel the current scheduled task if there are no schedules
        if (projectionList.isEmpty()) {
            if (scheduledReminder != null) {
                scheduledReminder.cancel(false);
            }
            return;
        }

        // Cancel the current scheduled task
        if (scheduledReminder != null) {
            scheduledReminder.cancel(false);
        }

        // Schedule reminders for each medication schedule
        for (MedicationScheduleProjection projection : projectionList) {
            LocalDateTime nextScheduleDateTime = projection.getDateTime();
            LocalDateTime now = LocalDateTime.now();
            long delayInSeconds = Duration.between(now, nextScheduleDateTime).getSeconds();

            // Schedule the reminder for the specific medication schedule
            scheduledReminder = taskScheduler.schedule(this::notifyScheduleTimeToUser, Instant.now().plusSeconds(delayInSeconds));
        }
    }


}
