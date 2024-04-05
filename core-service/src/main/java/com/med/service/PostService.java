package com.med.service;

import com.med.model.Post;
import com.med.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    public Page<Post> getAllPost(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return postRepository.findAll(pageRequest);
    }

    public List<Post> getByCategoryId(int id, int size) {
        PageRequest pageable = PageRequest.of(0, size);
        List<Post> posts = postRepository.getPostByCategoryId(id, pageable);
        return posts;
    }

    public Page<Post> getRandomPosts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return postRepository.getRandomPosts(pageRequest);
    }

    public Post updatePost(int id, String audio) throws Exception {
        Optional<Post> p = postRepository.findById(id);
        if (p.isPresent()) {
            Post post = p.get();
            post.setAudio(audio);
            return postRepository.save(post);
        } else {
            throw new Exception("Post not found");
        }
    }
}