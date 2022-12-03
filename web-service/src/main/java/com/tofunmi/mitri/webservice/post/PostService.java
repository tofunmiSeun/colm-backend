package com.tofunmi.mitri.webservice.post;

import com.tofunmi.mitri.usermanagement.profile.ProfileOverview;
import com.tofunmi.mitri.usermanagement.profile.ProfileService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created By tofunmi on 15/07/2022
 */
@Service
public class PostService {
    private final PostRepository repository;
    private final ProfileService profileService;
    private final PostReactionService postReactionService;

    public PostService(PostRepository repository, ProfileService profileService, PostReactionService postReactionService) {
        this.repository = repository;
        this.profileService = profileService;
        this.postReactionService = postReactionService;
    }

    public void createPost(String content, String profileId) {
        Post post = new Post();
        post.setContent(content);
        post.setAuthor(profileId);
        post.setCreatedOn(Instant.now());
        repository.save(post);
    }

    public List<PostViewModel> getForProfile(String profileId) {
        Sort sort = Sort.by("createdOn").descending();
        List<Post> posts = repository.findAll(sort);

        ProfileOverview profileOverview = profileService.getProfile(profileId);
        Set<String> postsLikedByProfile = postReactionService.getIdsForLikedPosts(profileId);

        return posts.stream().map(e -> new PostViewModel(e.getId(),
                        e.getContent(), profileOverview.getUsername(),
                        profileOverview.getName(), postsLikedByProfile.contains(e.getId())))
                .collect(Collectors.toList());
    }
}
