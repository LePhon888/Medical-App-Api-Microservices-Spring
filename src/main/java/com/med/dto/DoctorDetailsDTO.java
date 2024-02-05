package com.med.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class DoctorDetailsDTO implements Serializable {
    private String category;
    private String title;
    private String place;
    private Date fromDate;
    private Date toDate;

    public DoctorDetailsDTO(Object[] row) {
        this.category = (String) row[0];
        this.title = (String) row[1];
        this.place = (String) row[2];
        this.fromDate = (Date) row[3];
        this.toDate = (Date) row[4];
    }
}
