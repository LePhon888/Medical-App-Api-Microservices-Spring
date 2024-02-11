package com.med.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class RatingDTO implements Serializable {
    private String userName;
    private String userImage;
    private int star;
    private String comment;
    private Timestamp createdDate;
}
