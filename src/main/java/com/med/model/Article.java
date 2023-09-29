package com.med.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private int id;
    private String header;
    private String author;
    private String authorImage;
    private String date;
    private String image;
    private String content;
}
