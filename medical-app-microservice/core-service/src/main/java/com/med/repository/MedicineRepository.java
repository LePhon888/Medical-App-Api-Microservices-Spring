package com.med.repository;

import com.med.model.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
    Page<Medicine> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
