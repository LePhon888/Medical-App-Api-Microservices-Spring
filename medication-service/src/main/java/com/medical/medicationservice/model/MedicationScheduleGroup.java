package com.medical.medicationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "medication_schedule_group")
public class MedicationScheduleGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "hospital")
    private String hospital;

    @Column(name = "doctor")
    private String doctor;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "user_id")
    private Integer userId;
}
