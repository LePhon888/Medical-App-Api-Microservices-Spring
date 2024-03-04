package com.med.controller;

import com.med.model.Hour;
import com.med.model.Post;
import com.med.service.HourService;
import com.med.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public List<Post> getAll () {
        return this.postService.getAllPost();
    }

    @GetMapping("/{id}")
    public List<Post> getByCategoryId(@PathVariable(value = "id") int id) {
        return this.postService.getByCategoryId(id);
    }
}
