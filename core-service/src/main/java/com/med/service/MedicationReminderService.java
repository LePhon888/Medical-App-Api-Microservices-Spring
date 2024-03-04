package com.med.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.med.dto.MedicationScheduleDetailProjection;
import com.med.model.ScheduleTime;
import com.med.service.ScheduleTimeService;
import com.med.utils.Screen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MedicationReminderService {


}
