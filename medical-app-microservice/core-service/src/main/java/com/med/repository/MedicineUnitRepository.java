package com.med.repository;

import com.med.model.Medicine;
import com.med.model.MedicineUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineUnitRepository extends JpaRepository<MedicineUnit, Integer> {
}
