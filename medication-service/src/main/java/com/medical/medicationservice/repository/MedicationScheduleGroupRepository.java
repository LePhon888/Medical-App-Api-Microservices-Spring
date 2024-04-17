package com.medical.medicationservice.repository;


import com.medical.medicationservice.model.MedicationScheduleGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationScheduleGroupRepository extends JpaRepository<MedicationScheduleGroup, Integer> {
}
