package com.med.firebase.model;

import com.med.firebase.converter.ClickActionConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification", schema = "medicalapp")
public class Notification implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "title")
    private String title;
    @Column(name = "body")
    private String body;
    @Column(name = "is_read")
    private Boolean isRead;
    @Convert(converter = ClickActionConverter.class)
    @Column(name = "click_action")
    private Object clickAction;
    @Column(name = "sent_on")
    private LocalDateTime sentOn;
}
