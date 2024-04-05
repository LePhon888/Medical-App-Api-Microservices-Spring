package com.medical.medicationservice.service;

import com.medical.medicationservice.dto.CreateMedicationScheduleDTO;
import com.medical.medicationservice.dto.CreateMedicationScheduleGroup;
import com.medical.medicationservice.dto.MedicationScheduleDTO;
import com.medical.medicationservice.dto.ScheduleTimeDTO;
import com.medical.medicationservice.model.MedicationSchedule;
import com.medical.medicationservice.model.MedicationScheduleGroup;
import com.medical.medicationservice.repository.MedicationScheduleGroupRepository;
import com.medical.medicationservice.repository.MedicationScheduleRepository;
import com.medical.medicationservice.repository.ScheduleTimeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MedicationScheduleGroupService {
    @Autowired
    private MedicationScheduleGroupRepository repository;
    @Autowired
    private MedicationScheduleRepository medicationScheduleRepository;
    @Autowired
    private MedicationScheduleService medicationScheduleService;
    @Autowired
    private ScheduleTimeRepository scheduleTimeRepository;

    @Transactional
    public ResponseEntity<?> createOrUpdate(CreateMedicationScheduleGroup payload) {
        try {
            Optional<MedicationScheduleGroup> existingGroupRecord = repository.findById(payload.getId());

            if (existingGroupRecord.isPresent()) {

                // Proceed to updated group record
                MedicationScheduleGroup oldGroupRecord = existingGroupRecord.get();
                oldGroupRecord.setName(payload.getGroupName());
                oldGroupRecord.setHospital(payload.getHospitalName());
                oldGroupRecord.setIsActive(payload.getIsActive());

                List<Integer> oldScheduleIds = medicationScheduleRepository.getMedicationScheduleByGroupId(payload.getId())
                                .stream().map(MedicationScheduleDTO::getId)
                                .toList();

                List<Integer> newScheduleIds =
                        payload.getMedicineList()
                                .stream().map(CreateMedicationScheduleDTO::getId)
                                .toList();

                // Determine old IDs not in new list
                Set<Integer> idsToDelete = oldScheduleIds.stream()
                        .filter(id -> !newScheduleIds.contains(id))
                        .collect(Collectors.toSet());

                // Delete records not present in the new list
                idsToDelete.forEach(medicationScheduleRepository::deleteById);

                // Process new/updated schedules
                payload.getMedicineList()
                        .forEach(item -> {
                            item.setGroupId(oldGroupRecord.getId());
                            item.setIsActive(payload.getIsActive());
                            medicationScheduleService.createOrUpdateMedicationSchedule(item);
                        });

                repository.save(oldGroupRecord);

            } else {
                MedicationScheduleGroup createdRecord = new MedicationScheduleGroup(
                        0,
                        payload.getGroupName(),
                        payload.getHospitalName(),
                        payload.getDoctorName(),
                        payload.getIsActive(),
                        payload.getUserId()
                );

                MedicationScheduleGroup savedRecord = repository.save(createdRecord);

                payload.getMedicineList()
                        .forEach(item -> {
                            item.setGroupId(savedRecord.getId());
                            medicationScheduleService.createOrUpdateMedicationSchedule(item);
                        });

            }
            return ResponseEntity.ok("Created or Updated MedicationScheduleGroup successfully!");
        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseEntity.internalServerError().body("An error occurred while processing your request.");
        }
    }

    public CreateMedicationScheduleGroup getById(Integer id) {

        Optional<MedicationScheduleGroup> checkRecord = repository.findById(id);

        if (checkRecord.isEmpty()) {
            return null;
        }

        MedicationScheduleGroup record = checkRecord.get();

        List<CreateMedicationScheduleDTO> medicationScheduleList = new ArrayList<>();

        medicationScheduleRepository.getMedicationScheduleByGroupId(record.getId()).forEach(ms -> {
            List<ScheduleTimeDTO> scheduleTimeList = scheduleTimeRepository.
                    getScheduleTimeByMedicationScheduleId(ms.getId());

            medicationScheduleList.add(
                    new CreateMedicationScheduleDTO(
                            ms.getId(),
                            ms.getUserId(),
                            ms.getMedicine(),
                            ms.getMedicineUnit(),
                            ms.getStartDate(),
                            ms.getFrequency(),
                            ms.getSelectedDays(),
                            ms.getIsActive(),
                            ms.getGroupId(),
                            scheduleTimeList
                    )
            );

        });

        return new CreateMedicationScheduleGroup(
                record.getId(),
                record.getUserId(),
                record.getName(),
                record.getHospital(),
                record.getDoctor(),
                record.getIsActive(),
                medicationScheduleList
        );

    }

}
