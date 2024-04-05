package com.med.repository;

import com.med.dto.FeeMinMax;
import com.med.model.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeeRepository extends JpaRepository<Fee, Integer> {
    Fee findFirstByOrderByIdDesc();

    @Query("SELECT new com.med.dto.FeeMinMax(MIN(f.fee), MAX(f.fee)) FROM Fee f")
    FeeMinMax getFeeMinMax();

}
