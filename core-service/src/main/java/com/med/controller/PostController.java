package com.med.controller;

import com.med.model.Hour;
import com.med.model.Post;
import com.med.service.HourService;
import com.med.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public Page<Post> getAll (@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        return this.postService.getAllPost(page, size);
    }

    @GetMapping("/{id}")
    public List<Post> getByCategoryId(@PathVariable(value = "id") int id,
                                      @RequestParam(defaultValue = "10") int size) {
        return this.postService.getByCategoryId(id, size);
    }

    @GetMapping("/random")
    public Page<Post> getRandomPosts(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return this.postService.getRandomPosts(page, size);
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable(value = "id") int id,
                           @RequestParam String audio) throws Exception {
        return this.postService.updatePost(id, audio);
    }

}