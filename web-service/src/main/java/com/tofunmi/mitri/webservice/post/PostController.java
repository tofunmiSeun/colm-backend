package com.tofunmi.mitri.webservice.post;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Created By tofunmi on 15/07/2022
 */
@RestController
@RequestMapping("post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public void newPost(@RequestBody CreatePostRequest request, Principal principal) {
        postService.createPost(request.getContent(), principal.getName());
    }

    @GetMapping
    public List<Post> getAllPosts(Principal principal) {
        return postService.getForUser(principal.getName());
    }
}
