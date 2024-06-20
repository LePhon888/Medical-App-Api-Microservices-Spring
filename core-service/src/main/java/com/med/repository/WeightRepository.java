package com.med.repository;

import com.med.dto.WeightDTO;
import com.med.model.Weight;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public interface WeightRepository extends JpaRepository<Weight, Integer>{
    @Query("SELECT  new com.med.dto.WeightDTO(w.id, w.number, w.date, w.user.id , w.time, w.bmi, w.bmr, w.height, w.classification) FROM Weight w WHERE w.user.id = :userId order by w.id desc")
    List<WeightDTO> getWeightsByUserId(@Param("userId") Integer userId);

    @Query("SELECT new com.med.dto.WeightDTO(w.id, w.number, w.date, w.user.id , w.time, w.bmi, w.bmr, w.height, w.classification) " +
            "FROM Weight w " +
            "WHERE w.user.id = :userId AND w.time = (SELECT max(w1.time) FROM Weight w1 WHERE w1.user.id = :userId AND w1.date = w.date) AND w.date >= :startDate " +
            "ORDER BY w.id")
    List<WeightDTO> getWeightsByUserIdWeek(@Param("userId") Integer userId, @Param("startDate") LocalDate startDate);

    @Query("SELECT new com.med.dto.WeightDTO(w.id, w.number, w.date, w.user.id , w.time, w.bmi, w.bmr, w.height, w.classification) " +
            "FROM Weight w " +
            "WHERE w.user.id = :userId AND w.time = (SELECT max(w1.time) FROM Weight w1 WHERE w1.user.id = :userId AND w1.date = w.date) AND w.date >= :startDate " +
            "ORDER BY w.id")
    List<WeightDTO> getWeightsByUserIdMonth(@Param("userId") Integer userId, @Param("startDate") LocalDate startDate);

    @Query("SELECT new com.med.dto.WeightDTO(w.id, w.number, w.date, w.user.id , w.time, w.bmi, w.bmr, w.height, w.classification) " +
            "FROM Weight w " +
            "WHERE w.user.id = :userId AND w.time = (SELECT max(w1.time) FROM Weight w1 WHERE w1.user.id = :userId AND w1.date = w.date) AND w.date >= :startDate " +
            "ORDER BY w.id")
    List<WeightDTO> getWeightsByUserIdYear(@Param("userId") Integer userId, @Param("startDate") LocalDate startDate);

    @Query("SELECT new com.med.dto.WeightDTO(w.id, w.number, w.date, w.user.id, w.time, w.bmi, w.bmr, w.height, w.classification) " +
            "FROM Weight w WHERE w.user.id = :userId ORDER BY w.date DESC, w.time DESC")
    List<WeightDTO> getNewWeight(@Param("userId") Integer userId, Pageable pageable);

}
