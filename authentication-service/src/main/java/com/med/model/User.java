package com.med.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "first_name", nullable = true, length = 50)
    private String firstName;
    @Basic
    @Column(name = "last_name", nullable = true, length = 50)
    private String lastName;
    @Basic
    @Column(name = "birthday", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @Basic
    @Column(name = "address", nullable = true, length = 100)
    private String address;
    @Basic
    @Column(name = "gender", nullable = true)
    private Integer gender;
    @Basic
    @Column(name = "phone_number", nullable = true, length = 15)
    private String phoneNumber;
    @Basic
    @Column(name = "email", nullable = true, length = 30)
    private String email;
    @Basic
    @Column(name = "password", nullable = true, length = 100)
    private String password;
    @Basic
    @Column(name = "is_active", nullable = true)
    private Short isActive;
    @Basic
    @Column(name = "user_role", nullable = true, length = 45)
    private String userRole;

    @Column(name = "auth_provider")
    private String provider;
    @Basic
    @Column(name = "enabled", nullable = true)
    private boolean enabled;
    @Basic
    @Column(name = "image", nullable = true, length = 255)
    private String image;
    @Transient
    @JsonIgnore
    private MultipartFile file;

    @Column(name = "code")
    private Integer code;

    @Column(name = "created_date_code")
    private Date createdDateCode;
}