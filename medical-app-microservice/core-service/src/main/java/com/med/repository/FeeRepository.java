package com.med.repository;

import com.med.model.Fee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeRepository extends JpaRepository<Fee, Integer> {
    Fee findFirstByOrderByIdDesc();
}
