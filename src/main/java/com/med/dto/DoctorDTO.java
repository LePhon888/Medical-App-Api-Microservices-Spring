package com.med.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DoctorDTO {
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

    public DoctorDTO(Object[] row) {
        this.userId = (Integer) row[0];
        this.fullName = (String) row[1];
        this.image = (String) row[2];
        this.departmentName = (String) row[3];
        this.hospital = (String) row[4];
        this.hospitalAddress = (String) row[5];
        this.information = (String) row[6];
        this.consultation = (String) row[7];
        this.target = (String) row[8];
        this.fee = (BigDecimal) row[9];
        this.rating = (Double) row[10];
    }
}
