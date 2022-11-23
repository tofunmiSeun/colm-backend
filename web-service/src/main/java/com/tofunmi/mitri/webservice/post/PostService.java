package com.tofunmi.mitri.webservice.post;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Created By tofunmi on 15/07/2022
 */
@Service
public class PostService {
    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public void createPost(String content, String userId) {
        Post post = new Post();
        post.setContent(content);
        post.setAuthor(userId);
        post.setCreatedOn(Instant.now());
        repository.save(post);
    }

    public List<Post> getForUser(String name) {
        Sort sort = Sort.by("createdOn").descending();
        return repository.findAll(sort);
    }
}
