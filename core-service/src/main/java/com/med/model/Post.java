package com.med.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "header")
    private String header;

    @Lob
    @Column(name = "content")
    private String content;

    @Size(max = 200)
    @Column(name = "video", length = 200)
    private String video;

    @Lob
    @Column(name = "created_date")
    private String createdDate;

    @Size(max = 200)
    @Column(name = "image", length = 200)
    private String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @Size(max = 100)
    @Column(name = "author", length = 100)
    private String author;

    @Lob
    @Column(name = "audio")
    private String audio;

}