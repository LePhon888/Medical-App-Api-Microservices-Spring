package com.med.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DoctorDTO implements Serializable {
    private Integer userId;
    private String fullName;
    private String image;
    private String departmentName;
    private String hospital;
    private String hospitalAddress;
    private String information;
    private String consultation;
    private String target;
    private BigDecimal fee;
    private Double rating;
}
