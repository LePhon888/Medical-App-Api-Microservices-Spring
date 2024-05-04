package com.med.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

}