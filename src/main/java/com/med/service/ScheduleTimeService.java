package com.med.service;

import com.med.dto.MedicationScheduleDetailProjection;
import com.med.dto.ScheduleTimeDTO;
import com.med.firebase.dto.NotificationRequest;
import com.med.firebase.model.UserDevice;
import com.med.firebase.service.NotificationService;
import com.med.firebase.service.UserDeviceService;
import com.med.repository.ScheduleTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ScheduleTimeService {
    @Autowired
    private ScheduleTimeRepository repository;

    public List<MedicationScheduleDetailProjection> getScheduleTime(Integer userId, LocalDate startDate) {
        return this.repository.getScheduleTime(userId, startDate);
    }

    public List<ScheduleTimeDTO> getScheduleTimeByMedicationScheduleId(Integer scheduleId) {
        return this.repository.getScheduleTimeByMedicationScheduleId(scheduleId);
    }

   public List<MedicationScheduleDetailProjection> getScheduleTimeToSendNotification() {
        return this.repository.getScheduleTimeToSendNotification();
    }
}
