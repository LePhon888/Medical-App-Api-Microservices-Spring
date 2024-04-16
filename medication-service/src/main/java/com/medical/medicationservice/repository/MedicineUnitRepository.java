package com.medical.medicationservice.repository;

import com.medical.medicationservice.model.MedicineUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineUnitRepository extends JpaRepository<MedicineUnit, Integer> {
}
