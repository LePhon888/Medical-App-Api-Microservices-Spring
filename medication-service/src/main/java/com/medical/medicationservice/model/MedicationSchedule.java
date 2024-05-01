package com.medical.medicationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "medication_schedule")
@AllArgsConstructor
@NoArgsConstructor
public class MedicationSchedule implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "start_date", nullable = true)
    private LocalDate startDate;
    @Basic
    @Column(name = "frequency", nullable = true)
    private Integer frequency;
    @Basic
    @Column(name = "selected_days", nullable = true, length = 50)
    private String selectedDays;
    @Column(name = "user_id")
    private Integer userId;
    @ManyToOne
    @JoinColumn(name = "medicine_id", referencedColumnName = "id")
    private Medicine medicine;
    @ManyToOne
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    private MedicineUnit medicineUnit;
    @Basic
    @Column(name = "is_active", nullable = true)
    private Boolean isActive;
    @Basic
    @Column(name = "group_id", nullable = true)
    private Integer groupId;
}
