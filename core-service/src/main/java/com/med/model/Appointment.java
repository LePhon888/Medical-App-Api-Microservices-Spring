package com.med.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;


/**
 *
 * @author admin
 */
@Entity
@Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "reason")
    private String reason;
    @Column(name = "report_image")
    private String reportImage;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(name = "is_confirm")
    private Short isConfirm;
    @Column(name = "is_paid")
    private Short isPaid;
    @Column(name = "payment_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentTime;
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    @ManyToOne
    private Doctor doctor;
    @JoinColumn(name = "fee_id", referencedColumnName = "id")
    @ManyToOne
    private Fee fee;
    @JoinColumn(name = "hour_id", referencedColumnName = "id")
    @ManyToOne
    private Hour hour;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User user;
    @JoinColumn(name = "register_user_id", referencedColumnName = "id")
    @ManyToOne
    private User registerUser;
}
