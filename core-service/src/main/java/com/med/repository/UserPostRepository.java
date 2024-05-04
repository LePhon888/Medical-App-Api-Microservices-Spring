package com.med.repository;

import com.med.dto.UserPostDTO;
import com.med.model.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserPostRepository extends JpaRepository<UserPost, Integer>{
    @Query("SELECT new  com.med.dto.UserPostDTO(up.id, up.user.id, up.post) FROM UserPost up WHERE up.user.id = :userId ORDER BY up.id DESC")
    List<UserPostDTO> findByUserId(Integer userId);

    @Query("SELECT up FROM UserPost up WHERE up.user.id = :userId AND up.post.id = :postId")
    UserPost findByUserIdPostId(Integer userId, Integer postId);
}
