package com.med.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class DoctorDetailDTO implements Serializable {
    private String category;
    private String title;
    private String place;
    private Date fromDate;
    private Date toDate;
}
