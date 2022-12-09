package com.tofunmi.mitri.webservice.post;

import com.tofunmi.mitri.usermanagement.profile.ProfileOverview;
import com.tofunmi.mitri.usermanagement.profile.ProfileService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.Map;
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

    public void createPost(CreatePostRequest createPostRequest, String profileId) {
        repository.save(newPost(createPostRequest, profileId));
    }

    public void replyToPost(String originalPostId, CreatePostRequest createPostRequest, String profileId) {
        Assert.hasText(originalPostId, "Invalid post id to reply");
        Assert.isTrue(repository.existsById(originalPostId), String.format("No post with id %s", originalPostId));

        Post post = newPost(createPostRequest, profileId);
        post.setParentPostId(originalPostId);

        repository.save(post);
    }

    private Post newPost(CreatePostRequest createPostRequest, String profileId) {
        final String content = createPostRequest.getContent();
        Assert.hasText(content, "Content cannot be empty");

        Post post = new Post();
        post.setContent(content);
        post.setAuthor(profileId);
        post.setCreatedOn(Instant.now());

        return post;
    }

    public List<PostViewModel> getForProfile(String profileId) {
        Sort sort = Sort.by("createdOn").descending();
        List<Post> posts = repository.findAll(sort).stream()
                .filter(e -> !StringUtils.hasText(e.getParentPostId()))
                .collect(Collectors.toList());

        Set<String> uniqueProfileIds = posts.stream().map(Post::getAuthor).collect(Collectors.toSet());
        Map<String, ProfileOverview> profileOverviewMapping = profileService.getProfiles(uniqueProfileIds).stream()
                .collect(Collectors.toMap(ProfileOverview::getId, item -> item));

        Set<String> postsLikedByProfile = postReactionService.getIdsForLikedPosts(profileId);

        return posts.stream().map(e -> new PostViewModel(e.getId(),
                        e.getContent(), profileOverviewMapping.get(e.getAuthor()).getUsername(),
                        profileOverviewMapping.get(e.getAuthor()).getName(),
                        postsLikedByProfile.contains(e.getId())))
                .collect(Collectors.toList());
    }
}
