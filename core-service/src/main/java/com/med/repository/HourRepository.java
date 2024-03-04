package com.med.repository;

import com.med.model.Hour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HourRepository extends JpaRepository<Hour, Integer> {
}
