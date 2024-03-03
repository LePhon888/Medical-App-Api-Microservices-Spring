package com.med.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.med.dto.MedicationScheduleDetailProjection;
import com.med.firebase.dto.NotificationRequest;
import com.med.firebase.model.UserDevice;
import com.med.firebase.service.NotificationService;
import com.med.firebase.service.UserDeviceService;
import com.med.model.ScheduleTime;
import com.med.service.ScheduleTimeService;
import com.med.utils.Screen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NotifyMedicationSchedule {

    @Autowired
    private ScheduleTimeService scheduleTimeService;

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private NotificationService notificationService;

    @Value("${schedule.enable}")
    private boolean isScheduledEnabled;

    @Scheduled(fixedRate = 60000) // Set schedule every minute. Khuong: will consider to increase to 5 minutes.
    public void notifyScheduleTimeToUser() {

        if (!isScheduledEnabled) {
            return;
        }

        List<MedicationScheduleDetailProjection> list = scheduleTimeService.getScheduleTimeToSendNotification();
        if (!list.isEmpty()) {
            list.forEach((s) -> {
                // Check whether the user device already existed
                UserDevice userDevice = userDeviceService.getUserDeviceByUserId(s.getId());
                if (userDevice != null) {

                    // Add params when click the notification
                    Map<String, String> clickActionParams = new HashMap<>();
                    clickActionParams.put("screen", Screen.MedicationBox.getName());
                    clickActionParams.put("startDate", LocalDate.now().toString());

                    // Create a notification with format
                    notificationService.sendNotificationToUser(new NotificationRequest(
                            userDevice.getTokenRegistration(),
                            "Từ hộp thuốc của bạn ",
                            String.format("Đã đến giờ uống thuốc %s: %s %s vào lúc %s",
                                    s.getMedicineName(),
                                    s.getQuantity(),
                                    s.getUnitName(),
                                   s.getTime().format(DateTimeFormatter.ofPattern("hh:mm a"))),
                            "",
                            s.getId(),
                            clickActionParams));
                }
            });
        }
    }
}
