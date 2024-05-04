package com.med.dto;

import com.med.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserPostDTO {
    private String id;
    private String userId;
    private Post post;
    //constructor
    public UserPostDTO(Integer id, Integer userId, Post post) {
        this.id = id.toString();
        this.userId = userId.toString();
        this.post = post;
    }
}
