package com.med.repository;

import com.med.model.Rating;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    @Query("SELECT " +
            "concat(r.user.lastName, ' ', r.user.firstName)," +
            "r.user.image, " +
            "r.star," +
            "r.comment, " +
            "r.createdDate " +
            "FROM Rating r " +
            "WHERE r.doctor.id = cast(:id as int) " +
            "ORDER BY r.createdDate DESC")
    List<Object[]> getRatingByDoctorId(String id);
    @Query("SELECT r.star, COUNT (*) " +
            "FROM Rating r " +
            "WHERE r.doctor.id = CAST(:id as int) " +
            "GROUP BY r.star " +
            "ORDER BY r.star DESC ")
    List<Object[]> getRatingStatsByDoctorId(String id);
}
