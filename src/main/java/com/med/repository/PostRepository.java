package com.med.repository;

import com.med.model.Doctor;
import com.med.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT p FROM Post p WHERE p.category.id = :catId")
    List<Post> getPostByCategoryId(@Param("catId") int catId);
    List<Post> findTop10ByOrderByCreatedDateDesc();

}


