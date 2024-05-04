package com.med.service;

import com.med.dto.UserPostDTO;
import com.med.model.UserPost;
import com.med.repository.UserPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPostService {
    @Autowired
    private UserPostRepository userPostRepository;

    public void saveUserPost(UserPost userPost) {
        userPostRepository.save(userPost);
    }

    public void deleteUserPost(UserPost userPost) {
        userPostRepository.delete(userPost);
    }

    public UserPost getUserPostById(Integer id) {
        return userPostRepository.findById(id).orElse(null);
    }

    public List<UserPostDTO> getUserPostByUserId(Integer userId) {
        return userPostRepository.findByUserId(userId);
    }

    public UserPost getUserPostByUserIdPostId(Integer userId, Integer postId) {
        return userPostRepository.findByUserIdPostId(userId, postId);
    }
}
