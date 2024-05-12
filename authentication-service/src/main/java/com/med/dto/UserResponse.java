package com.med.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private Date birthday;
    private String address;
    private Integer gender;
    private String phoneNumber;
    private String email;
    private String image;
    private String userRole;

}
