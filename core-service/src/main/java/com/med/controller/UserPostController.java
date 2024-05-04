package com.med.controller;

import com.med.dto.UserPostDTO;
import com.med.model.Post;
import com.med.model.User;
import com.med.model.UserPost;
import com.med.service.PostService;
import com.med.service.UserPostService;
import com.med.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/user-post")
public class UserPostController {
    @Autowired
    private UserPostService userPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<List<UserPostDTO>> getAllUserPostsByUserId(@PathVariable Integer id) {
        return ResponseEntity.ok(userPostService.getUserPostByUserId(id));
    }

    @PostMapping
    public void saveUserPost(@RequestBody Map<String, String> params) {
        User u = this.userService.getById(Integer.parseInt(params.get("userId")));
        Post p = this.postService.getById(Integer.parseInt(params.get("postId")));
        UserPost userPost = new UserPost();
        userPost.setUser(u);
        userPost.setPost(p);
        userPostService.saveUserPost(userPost);
    }

    @DeleteMapping("/delete")
    public void deleteUserPost(@RequestParam("userId") Integer userId, @RequestParam("postId") Integer postId) {
        UserPost up = this.userPostService.getUserPostByUserIdPostId(userId, postId);
        userPostService.deleteUserPost(userPostService.getUserPostById(up.getId()));
    }
}
