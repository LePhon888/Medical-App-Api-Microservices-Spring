package com.med.service;

import com.med.model.Post;
import com.med.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    public List<Post> getAllPost() {
        return postRepository.findTop10ByOrderByCreatedDateDesc();
    }

    public List<Post> getByCategoryId(int id) { return postRepository.getPostByCategoryId(id);}
}
