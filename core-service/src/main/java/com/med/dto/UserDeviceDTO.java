package com.med.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserDeviceDTO {
    private Integer userId;
    private String tokenRegistration;
}
