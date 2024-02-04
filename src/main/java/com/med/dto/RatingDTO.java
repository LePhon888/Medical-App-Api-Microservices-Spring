package com.med.dto;

import com.google.api.client.util.DateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class RatingDTO {
    private String userName;
    private String userImage;
    private int star;
    private String comment;
    private Timestamp createdDate;

    public RatingDTO(Object[] row) {
        this.userName = (String) row[0];
        this.userImage = (String) row[1];
        this.star = (int) row[2];
        this.comment = (String) row[3];
        this.createdDate = (Timestamp) row[4];
    }

}
