package com.med.repository;

import com.med.dto.MedicationScheduleDetailProjection;
import com.med.model.ScheduleTime;
import com.med.model.ScheduleTimeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleTimeDetailRepository extends JpaRepository<ScheduleTimeDetail, Integer> {

}
