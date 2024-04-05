package com.med.repository;

import com.med.dto.WeightDTO;
import com.med.model.Weight;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public interface WeightRepository extends JpaRepository<Weight, Integer>{
    Logger logger = Logger.getLogger(WeightRepository.class.getName());

    @Query("SELECT  new com.med.dto.WeightDTO(w.id, w.number, w.date, w.user.id , w.time, w.bmi, w.bmr, w.height) FROM Weight w WHERE w.user.id = :userId order by w.id desc")
    List<WeightDTO> getWeightsByUserId(@Param("userId") Integer userId);

    @Query("SELECT new com.med.dto.WeightDTO(w.id, w.number, w.date, w.user.id , w.time, w.bmi, w.bmr, w.height) " +
            "FROM Weight w WHERE w.user.id = :userId AND w.date >= :startDate")
    List<WeightDTO> getWeightsByUserIdWeek(@Param("userId") Integer userId, @Param("startDate") LocalDate startDate);

    @Query("SELECT new com.med.dto.WeightDTO(w.id, w.number, w.date, w.user.id , w.time, w.bmi, w.bmr, w.height) " +
            "FROM Weight w WHERE w.user.id = :userId AND w.date >= :startDate")
    List<WeightDTO> getWeightsByUserIdMonth(@Param("userId") Integer userId, @Param("startDate") LocalDate startDate);

}
