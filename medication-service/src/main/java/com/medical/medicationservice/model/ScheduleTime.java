package com.medical.medicationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "schedule_time", schema = "medicalapp")
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleTime implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "time", nullable = true)
    private LocalTime time;
    @Basic
    @Column(name = "quantity", nullable = true)
    private BigDecimal quantity;
    @ManyToOne
    @JoinColumn(name = "medication_schedule_id", referencedColumnName = "id")
    private MedicationSchedule medicationSchedule;

}
