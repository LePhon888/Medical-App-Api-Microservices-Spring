package com.med.service;

import com.med.dto.CreateOrUpdateMedicationScheduleDTO;
import com.med.dto.MedicationScheduleDTO;
import com.med.dto.MedicationScheduleProjection;
import com.med.dto.ScheduleTimeDTO;
import com.med.model.*;
import com.med.repository.*;
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
    private UserRepository userRepository;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private MedicineUnitRepository medicineUnitRepository;
    @Autowired
    private ScheduleTimeRepository scheduleTimeRepository;

    public ResponseEntity<String> createOrUpdateMedicationSchedule(CreateOrUpdateMedicationScheduleDTO payload) {
        try {
            // Check if the user, medicine, and medicine unit exist
            User user = userRepository.findById(payload.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

            String selectedDays = null;
            if (payload.getSelectedDays() != null) {
                selectedDays = payload.getSelectedDays().stream().map(String::valueOf)
                        .collect(Collectors.joining(","));
            }

            MedicationSchedule schedule;

            // Check if ID is provided in the payload
            if (payload.getId() != null && payload.getId() > 0) {
                // Update existing MedicationSchedule
                schedule = repository.findById(payload.getId())
                        .orElseThrow(() -> new RuntimeException("MedicationSchedule not found"));
                schedule.setStartDate(payload.getStartDate());
                schedule.setFrequency(payload.getFrequency());
                schedule.setSelectedDays(selectedDays);
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
                        selectedDays,
                        user,
                        payload.getMedicine(),
                        payload.getMedicineUnit(),
                        payload.getIsActive() != null ? payload.getIsActive() : true
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
            return this.repository.getMedicationScheduleByUserId(userId);
        } else {
            return this.repository.getInactiveMedicationScheduleByUserId(userId);
        }
    }

    public MedicationScheduleDTO getMedicationScheduleById(Integer id) {
        return this.repository.getMedicationScheduleById(id);
    }

}
