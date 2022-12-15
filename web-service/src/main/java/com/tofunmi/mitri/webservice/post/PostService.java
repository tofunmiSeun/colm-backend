package com.tofunmi.mitri.webservice.post;

import com.tofunmi.mitri.usermanagement.profile.ProfileViewModel;
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
        Post post = newPost(textContent, profileId, mediaContents);
        repository.save(post);
    }

    public void replyToPost(String originalPostId, String profileId, String textContent, MultipartFile[] mediaContents) {
        Assert.hasText(originalPostId, "Invalid post id to reply");
        Post originalPost = repository.findById(originalPostId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("No post with id %s", originalPostId)));
        Assert.isNull(originalPost.getDeletedAt(), "Post has been deleted");

        Post post = newPost(textContent, profileId, mediaContents);
        post.setParentPostId(originalPostId);

        repository.save(post);
    }

    private Post newPost(String content, String profileId, MultipartFile[] mediaContents) {
        Assert.isTrue(profileService.exists(profileId), String.format("Profile with id %s does not exist", profileId));
        boolean postContainsContent = (mediaContents != null && mediaContents.length > 0) ||
                StringUtils.hasText(content);
        Assert.isTrue(postContainsContent, "At least some text or one media content is required");

        Post post = new Post();
        post.setContent(content);
        post.setAuthor(profileId);
        post.setCreatedOn(Instant.now());

        List<SavedMediaContent> savedMediaContents = saveMediaContents(mediaContents);
        if (savedMediaContents.size() > 0) {
            post.setMediaContents(saveMediaContents(mediaContents));
        }

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
                .filter(e -> e.getDeletedAt() == null)
                .filter(e -> !StringUtils.hasText(e.getParentPostId()))
                .collect(Collectors.toList());

        return hydratePosts(posts, profileId);
    }

    public List<PostViewModel> getReplies(String id, String profileId) {
        List<Post> repliesToPost = repository.findAllByParentPostId(id, sort).stream()
                .filter(e -> e.getDeletedAt() == null)
                .collect(Collectors.toList());

        return hydratePosts(repliesToPost, profileId);
    }

    private List<PostViewModel> hydratePosts(List<Post> posts, String profileId) {
        Set<String> uniqueProfileIds = posts.stream().map(Post::getAuthor).collect(Collectors.toSet());
        Map<String, ProfileViewModel> profileOverviewMapping = profileService.getProfiles(uniqueProfileIds).stream()
                .collect(Collectors.toMap(ProfileViewModel::getId, item -> item));

        Set<String> postsLikedByProfile = postReactionService.getIdsForLikedPosts(profileId);

        return posts.stream().map(e -> new PostViewModel(e.getId(), e.getAuthor(),
                        e.getCreatedOn().toEpochMilli(),
                        e.getContent(), e.getMediaContents(),
                        profileOverviewMapping.get(e.getAuthor()).getUsername(),
                        profileOverviewMapping.get(e.getAuthor()).getName(),
                        postsLikedByProfile.contains(e.getId())))
                .collect(Collectors.toList());
    }

    public void deletePost(String id, String profileId) {
        Post post = repository.findById(id).orElseThrow();
        Assert.isTrue(Objects.equals(post.getAuthor(), profileId), "A profile cannot delete a post that it did not create");
        post.setDeletedAt(Instant.now());
        repository.save(post);
    }

    public Long countForProfile(String profileId) {
        return repository.countAllByAuthor(profileId);
    }
}
