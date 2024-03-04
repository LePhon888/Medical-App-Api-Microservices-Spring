package com.med.firebase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private String token;
    private String title;
    private String body;
    private String image;
    private Integer userId;
    private Map<String, String> clickActionParams;
}
