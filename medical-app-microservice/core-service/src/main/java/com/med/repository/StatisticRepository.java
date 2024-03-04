package com.med.repository;

import com.med.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Map;

public interface StatisticRepository extends JpaRepository<Appointment, Long> {

    @Query(
            "SELECT CASE WHEN :#{#params['month']} <> 0 THEN MONTH(a.date)" +
                    " WHEN :#{#params['quarter']} <> 0 THEN QUARTER(a.date) " +
                    "ELSE YEAR(a.date) END AS time, COUNT(a.user.id)" +
                    "FROM Appointment a " +
                    "WHERE a.isConfirm = 1 AND a.isPaid = 1 " +
                    "AND (:#{#params['year']} = 0 OR :#{#params['year']} = YEAR(a.date)) " +
                    "GROUP BY time  ORDER BY time ASC "
    )
    List<Object[]> countTotalPatientsByTime(@Param("params") Map<String, Object> params);

    @Query(nativeQuery = true, value =
            "WITH cte AS (" +
                    "    SELECT " +
                    "        CASE " +
                    "            WHEN :#{#params['month']} <> 0 THEN MONTH(a.date) " +
                    "            WHEN :#{#params['quarter']} <> 0 THEN QUARTER(a.date) " +
                    "            ELSE YEAR(a.date) " +
                    "        END AS time, " +
                    "        h.hour, " +
                    "        COUNT(a.user_id), " +
                    "        ROW_NUMBER() OVER (PARTITION BY " +
                    "            CASE " +
                    "                WHEN :#{#params['month']} <> 0 THEN MONTH(a.date) " +
                    "                WHEN :#{#params['quarter']} <> 0 THEN QUARTER(a.date) " +
                    "                ELSE YEAR(a.date) " +
                    "            END " +
                    "            ORDER BY COUNT(a.user_id) DESC) as RowNum " +
                    "    FROM appointment a " +
                    "    JOIN hour h ON h.id = a.hour_id " +
                    "    WHERE a.is_paid = 1 AND a.is_confirm = 1 " +
                    "        AND (:#{#params['year']} = 0 OR :#{#params['year']} = YEAR(a.date)) " +
                    "    GROUP BY time, h.hour " +
                    "    ORDER BY time ASC, h.hour ASC " +
                    ") " +
                    "SELECT * FROM cte WHERE RowNum <= 1")
    List<Object[]> countFrequentPatientVisitsByTime(
            @Param("params") Map<String, Object> params
    );




    @Query("SELECT CASE WHEN :#{#params['month']} <> 0 THEN MONTH(a.date)" +
            "WHEN :#{#params['quarter']} <> 0 THEN QUARTER(a.date) " +
            "ELSE YEAR(a.date) END AS time,  SUM(f.fee) " +
            "FROM Appointment a " +
            "JOIN Fee f on f.id = a.fee.id " +
            "WHERE a.isPaid = 1 AND a.isConfirm = 1 AND (:#{#params['year']} = 0 OR :#{#params['year']} = YEAR(a.date)) " +
            "GROUP BY time " +
            "ORDER BY time ASC ")
    List<Object[]> getRevenueByTime(@Param("params") Map<String, Object> params);





}
