package com.med.dto;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {
    private String token;
    private String title;
    private String body;
    private String image;
    private Integer userId;
    private Map<String, String> clickActionParams;
}