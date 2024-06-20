package com.med.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    //birthday
    private Date birthday;
    //constructor
    public UserDTO(Integer id, String firstName, String lastName, Date birthday) {
        this.id = id.toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
    }
}
