package com.medical.websocketservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Participant implements Serializable {
    private Integer userId;
    private String userName;
    private String sessionId;
    private LocalDateTime joinedAt;
}
