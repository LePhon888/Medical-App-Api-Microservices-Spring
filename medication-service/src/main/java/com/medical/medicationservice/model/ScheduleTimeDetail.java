package com.medical.medicationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedule_time_detail", schema = "medicalapp")
public class ScheduleTimeDetail {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "date")
    private LocalDate date;
    @Basic
    @Column(name = "is_used")
    private Boolean isUsed;
    @ManyToOne
    @JoinColumn(name = "schedule_time_id", referencedColumnName = "id")
    private ScheduleTime scheduleTime;

}
