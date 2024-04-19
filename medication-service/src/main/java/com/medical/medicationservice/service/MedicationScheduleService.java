package com.medical.medicationservice.service;
import com.medical.medicationservice.dto.CreateMedicationScheduleDTO;
import com.medical.medicationservice.dto.MedicationScheduleDTO;
import com.medical.medicationservice.dto.MedicationScheduleProjection;
import com.medical.medicationservice.dto.ScheduleTimeDTO;
import com.medical.medicationservice.model.MedicationSchedule;
import com.medical.medicationservice.model.ScheduleTime;
import com.medical.medicationservice.repository.MedicationScheduleRepository;
import com.medical.medicationservice.repository.ScheduleTimeRepository;
import com.medical.medicationservice.schedule.ReminderMedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MedicationScheduleService {
    @Autowired
    private MedicationScheduleRepository repository;
    @Autowired
    private ScheduleTimeRepository scheduleTimeRepository;

    public ResponseEntity<String> createOrUpdateMedicationSchedule(CreateMedicationScheduleDTO payload) {
        try {
            // Check if the user, medicine, and medicine unit exist
            //User user = userRepository.findById(payload.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));


            MedicationSchedule schedule;

            // Check if ID is provided in the payload
            if (payload.getId() != null && payload.getId() > 0) {
                // Update existing MedicationSchedule
                schedule = repository.findById(payload.getId())
                        .orElseThrow(() -> new RuntimeException("MedicationSchedule not found"));
                schedule.setStartDate(payload.getStartDate());
                schedule.setFrequency(payload.getFrequency());
                schedule.setSelectedDays(payload.getSelectedDays());
                schedule.setMedicine(payload.getMedicine());
                schedule.setMedicineUnit(payload.getMedicineUnit());
                schedule.setIsActive(payload.getIsActive());

                repository.save(schedule);

                // Get all existing items in the database
                List<ScheduleTimeDTO> existingScheduleTimes = scheduleTimeRepository.getScheduleTimeByMedicationScheduleId(payload.getId());

                // Get All item Ids in the updated list
                Set<Integer> updatedTimeIds = payload.getScheduleTimes().stream()
                        .map(ScheduleTimeDTO::getId)
                        .collect(Collectors.toSet());

                // Delete items not present in the updated list using the repository
                existingScheduleTimes.stream()
                        .filter(scheduleTimeDTO -> !updatedTimeIds.contains(scheduleTimeDTO.getId()))
                        .forEach(scheduleTimeDTO -> scheduleTimeRepository.deleteById(scheduleTimeDTO.getId()));

                // Create new items for items with id as null
                payload.getScheduleTimes().stream()
                        .filter(created -> created.getId() == 0)
                        .forEach(created -> {
                            ScheduleTime scheduleTime = new ScheduleTime(
                                    0,
                                    created.getTime(),
                                    created.getQuantity(),
                                    schedule
                            );
                            scheduleTimeRepository.save(scheduleTime);
                        });

            } else {
                // Create new MedicationSchedule
                schedule = new MedicationSchedule(
                        0,
                        payload.getStartDate(),
                        payload.getFrequency(),
                        payload.getSelectedDays(),
                        payload.getUserId(),
                        payload.getMedicine(),
                        payload.getMedicineUnit(),
                        payload.getIsActive() != null ? payload.getIsActive() : true,
                        payload.getGroupId()
                );

                // Save or update MedicationSchedule
                MedicationSchedule savedSchedule = repository.save(schedule);

                // Loop through scheduleTimes list and create MedicationScheduleTime in the database
                payload.getScheduleTimes().forEach(scheduleTime -> {
                    ScheduleTime object = new ScheduleTime(
                            0,
                            scheduleTime.getTime(),
                            scheduleTime.getQuantity(),
                            savedSchedule);
                    scheduleTimeRepository.save(object);
                });
            }

            return ResponseEntity.status(HttpStatus.CREATED).body("MedicationSchedule created or updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


    public List<MedicationScheduleProjection> getMedicationScheduleByUserId(Integer userId, Boolean isActive) {
        if (isActive) {
            return this.repository.getMedicationScheduleByUserId(userId, 9999);
        } else {
            return this.repository.getInactiveMedicationScheduleByUserId(userId);
        }
    }

    public List<MedicationScheduleProjection> getNextScheduleToReminder() {
        return this.repository.getMedicationScheduleByUserId(null, 99);
    }

    public MedicationScheduleDTO getMedicationScheduleById(Integer id) {
        MedicationScheduleDTO record = this.repository.getMedicationScheduleById(id);
        record.setScheduleTimes(scheduleTimeRepository.getScheduleTimeByMedicationScheduleId(id));
        return record;
    }

}
