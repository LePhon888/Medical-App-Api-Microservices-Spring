/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.med.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "doctor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "doctor")
    @JsonIgnore
    private Collection<Appointment> appointmentCollection;
    @Column(name = "hospital_address", nullable = true, length = 250)
    private String hospitalAddress;
    @Basic
    @Column(name = "information", nullable = true, length = 250)
    private String information;
    @ManyToOne
    @JoinColumn(name = "fee_id", referencedColumnName = "id")
    private Fee fee;
    @JsonIgnore
    @OneToMany(mappedBy = "doctor")
    private Collection<DoctorDetail> doctorDetailById;
    @Column(name = "hospital", nullable = true, length = 50)
    private String hospital;
    @JsonIgnore
    @OneToMany(mappedBy = "doctor")
    private Collection<Rating> ratings;

    @Size(max = 20)
    @Column(name = "title", length = 20)
    private String title;

}
