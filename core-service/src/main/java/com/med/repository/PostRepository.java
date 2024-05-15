package com.med.repository;

import com.med.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT p FROM Post p WHERE p.category.id = :catId ORDER BY RAND()")
    List<Post> getPostByCategoryId(@Param("catId") int catId, Pageable pageable);
    List<Post> findTop10ByOrderByCreatedDateDesc();

    @Query("SELECT * FROM Post ORDER BY RAND()")
    Page<Post> getRandomPosts(Pageable pageable);

}