package com.tofunmi.mitri.webservice.post;

import com.tofunmi.mitri.usermanagement.profile.ProfileOverview;
import com.tofunmi.mitri.usermanagement.profile.ProfileService;
import com.tofunmi.mitri.webservice.mediacontent.MediaContentService;
import com.tofunmi.mitri.webservice.mediacontent.SavedMediaContent;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created By tofunmi on 15/07/2022
 */
@Service
public class PostService {
    private final PostRepository repository;
    private final ProfileService profileService;
    private final PostReactionService postReactionService;
    private final MediaContentService mediaContentService;

    private final Sort sort = Sort.by("createdOn").descending();

    public PostService(PostRepository repository, ProfileService profileService,
                       PostReactionService postReactionService,
                       MediaContentService mediaContentService) {
        this.repository = repository;
        this.profileService = profileService;
        this.postReactionService = postReactionService;
        this.mediaContentService = mediaContentService;
    }

    public void createPost(String profileId, String textContent, MultipartFile[] mediaContents) {
        boolean postContainsContent = (mediaContents != null && mediaContents.length > 0) ||
                StringUtils.hasText(textContent);
        Assert.isTrue(postContainsContent, "At least some text or one media content is required");

        Post post = newPost(textContent, profileId, mediaContents);
        repository.save(post);
    }

    public void replyToPost(String originalPostId, String profileId, String textContent, MultipartFile[] mediaContents) {
        Assert.hasText(originalPostId, "Invalid post id to reply");
        Assert.isTrue(repository.existsById(originalPostId), String.format("No post with id %s", originalPostId));
        boolean postContainsContent = (mediaContents != null && mediaContents.length > 0) ||
                StringUtils.hasText(textContent);
        Assert.isTrue(postContainsContent, "At least some text or one media content is required");

        Post post = newPost(textContent, profileId, mediaContents);
        post.setParentPostId(originalPostId);

        repository.save(post);
    }

    private Post newPost(String content, String profileId, MultipartFile[] mediaContents) {
        Assert.isTrue(profileService.exists(profileId), String.format("Profile with id %s does not exist", profileId));

        Post post = new Post();
        post.setContent(content);
        post.setAuthor(profileId);
        post.setCreatedOn(Instant.now());
        post.setMediaContents(saveMediaContents(mediaContents));

        return post;
    }

    private List<SavedMediaContent> saveMediaContents(MultipartFile[] mediaContents) {
        List<SavedMediaContent> savedMediaContents = new ArrayList<>();
        if (mediaContents != null) {
            for (MultipartFile fileContent : mediaContents) {
                try {
                    savedMediaContents.add(mediaContentService.save(fileContent));
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save media content", e.getCause());
                }
            }
        }
        return savedMediaContents;
    }

    public List<PostViewModel> getForProfile(String profileId) {
        List<Post> posts = repository.findAll(sort).stream()
                .filter(e -> !StringUtils.hasText(e.getParentPostId()))
                .collect(Collectors.toList());

        return hydratePosts(posts, profileId);
    }

    public List<PostViewModel> getReplies(String id, String profileId) {
        List<Post> repliesToPost = repository.findAllByParentPostId(id, sort);
        return hydratePosts(repliesToPost, profileId);
    }

    public List<PostViewModel> hydratePosts(List<Post> posts, String profileId) {
        Set<String> uniqueProfileIds = posts.stream().map(Post::getAuthor).collect(Collectors.toSet());
        Map<String, ProfileOverview> profileOverviewMapping = profileService.getProfiles(uniqueProfileIds).stream()
                .collect(Collectors.toMap(ProfileOverview::getId, item -> item));

        Set<String> postsLikedByProfile = postReactionService.getIdsForLikedPosts(profileId);

        return posts.stream().map(e -> new PostViewModel(e.getId(),
                        e.getContent(), e.getMediaContents(),
                        profileOverviewMapping.get(e.getAuthor()).getUsername(),
                        profileOverviewMapping.get(e.getAuthor()).getName(),
                        postsLikedByProfile.contains(e.getId())))
                .collect(Collectors.toList());
    }
}
