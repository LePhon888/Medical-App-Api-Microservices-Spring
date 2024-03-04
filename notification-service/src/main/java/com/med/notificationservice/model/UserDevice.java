package com.med.notificationservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_device", schema = "medicalapp")
public class UserDevice implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "user_id")
    private Integer userId;
    @Basic
    @Column(name = "token_registration")
    private String tokenRegistration;
    @Basic
    @Column(name = "created_on")
    private LocalDateTime createdOn;
}
