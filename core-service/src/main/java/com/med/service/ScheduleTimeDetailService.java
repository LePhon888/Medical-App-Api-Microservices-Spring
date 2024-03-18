package com.med.service;

import com.med.dto.HistoryMedication;
import com.med.dto.ScheduleTimeDetailDTO;
import com.med.model.ScheduleTimeDetail;
import com.med.repository.ScheduleTimeDetailRepository;
import com.med.repository.ScheduleTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleTimeDetailService {
    @Autowired
    private ScheduleTimeDetailRepository repository;
    @Autowired
    private ScheduleTimeRepository scheduleTimeRepository;

    public ResponseEntity<String> createOrUpdate(ScheduleTimeDetailDTO dto) {
        Optional<ScheduleTimeDetail> existingDetail = repository.findById(dto.getId());

        if (existingDetail.isPresent()) {
            // If the detail exists, update its properties
            ScheduleTimeDetail detailToUpdate = existingDetail.get();
            detailToUpdate.setIsUsed(dto.getIsUsed());
            repository.save(detailToUpdate);
            return ResponseEntity.ok("Updated successfully");
        } else {
            // If the detail doesn't exist, create a new one
            ScheduleTimeDetail newDetail = new ScheduleTimeDetail(
                    0,
                    dto.getDate(),
                    dto.getIsUsed(),
                    scheduleTimeRepository.findById(dto.getScheduleTimeId())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid ScheduleTimeId"))
            );
            repository.save(newDetail);
            return ResponseEntity.status(HttpStatus.CREATED).body("Created successfully");
        }
    }

    public Page<HistoryMedication> getHistoryMedicationByUserId(Integer userId, Pageable pageable) {
        return this.repository.getHistoryMedicationByUserId(userId, pageable);
    }

}
