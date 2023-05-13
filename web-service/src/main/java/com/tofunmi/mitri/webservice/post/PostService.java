package com.tofunmi.mitri.webservice.post;

import com.tofunmi.colm.profile.ProfileViewModel;
import com.tofunmi.colm.profile.ProfileService;
import com.tofunmi.mitri.webservice.follows.FollowsService;
import com.tofunmi.mitri.webservice.mediacontent.MediaContentService;
import com.tofunmi.mitri.webservice.mediacontent.SavedMediaContent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
    private final FollowsService followsService;
    private final ApplicationEventPublisher publisher;

    private final Sort sort = Sort.by("createdOn").descending();

    public PostService(PostRepository repository, ProfileService profileService,
                       PostReactionService postReactionService,
                       MediaContentService mediaContentService,
                       FollowsService followsService, ApplicationEventPublisher publisher) {
        this.repository = repository;
        this.profileService = profileService;
        this.postReactionService = postReactionService;
        this.mediaContentService = mediaContentService;
        this.followsService = followsService;
        this.publisher = publisher;
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

        Post reply = newPost(textContent, profileId, mediaContents);
        reply.setParentPostId(originalPostId);
        reply = repository.save(reply);
        publisher.publishEvent(new PostReplyPublishedEvent(reply.getId(), reply.getAuthor(), originalPostId, originalPost.getAuthor()));
    }

    private Post newPost(String content, String profileId, MultipartFile[] mediaContents) {
        profileService.validateProfileExistence(profileId);
        boolean postContainsContent = (mediaContents != null && mediaContents.length > 0) ||
                StringUtils.hasText(content);
        Assert.isTrue(postContainsContent, "At least some text or one media content is required");

        Post post = new Post();
        post.setContent(content);
        post.setAuthor(profileId);
        post.setCreatedOn(Instant.now());
        post.setLikesCount(0L);

        List<SavedMediaContent> savedMediaContents = mediaContentService.save(mediaContents);
        if (savedMediaContents.size() > 0) {
            post.setMediaContents(savedMediaContents);
        }

        return post;
    }

    public List<PostViewModel> getForFeed(String profileId) {
        Set<String> followedProfiles = followsService.getFollowedProfiles(profileId);
        followedProfiles.add(profileId);

        List<Post> posts = repository.findAll(sort).stream()
                .filter(e -> e.getDeletedAt() == null)
                .filter(e -> !StringUtils.hasText(e.getParentPostId()))
                .filter(e -> followedProfiles.contains(e.getAuthor()))
                .collect(Collectors.toList());

        return hydratePosts(posts, profileId);
    }

    public List<PostViewModel> getReplies(String id, String profileId) {
        List<Post> repliesToPost = repository.findAllByParentPostId(id, sort).stream()
                .filter(e -> e.getDeletedAt() == null)
                .collect(Collectors.toList());

        return hydratePosts(repliesToPost, profileId);
    }

    public List<PostViewModel> hydratePosts(List<Post> posts, String profileId) {
        Set<String> uniqueProfileIds = posts.stream().map(Post::getAuthor).collect(Collectors.toSet());
        Map<String, ProfileViewModel> profileOverviewMapping = profileService.getProfiles(uniqueProfileIds).stream()
                .collect(Collectors.toMap(ProfileViewModel::getId, item -> item));

        Set<String> postsLikedByProfile = postReactionService.getIdsForLikedPosts(profileId);

        return posts.stream().map(e -> new PostViewModel(e.getId(), e.getAuthor(),
                        e.getCreatedOn().toEpochMilli(),
                        e.getContent(), e.getMediaContents(),
                        profileOverviewMapping.get(e.getAuthor()).getUsername(),
                        profileOverviewMapping.get(e.getAuthor()).getName(),
                        postsLikedByProfile.contains(e.getId()),
                        e.getLikesCount()))
                .collect(Collectors.toList());
    }

    public void deletePost(String id, String profileId) {
        Post post = repository.findById(id).orElseThrow();
        Assert.isTrue(Objects.equals(post.getAuthor(), profileId), "A profile cannot delete a post that it did not create");
        post.setDeletedAt(Instant.now());
        repository.save(post);
    }

    public List<PostViewModel> findForProfile(String profileId, String loggedInUserProfileId) {
        List<Post> repliesToPost = repository.findAllByAuthor(profileId, sort).stream()
                .filter(e -> e.getDeletedAt() == null)
                .collect(Collectors.toList());

        return hydratePosts(repliesToPost, loggedInUserProfileId);
    }

    public Long countLikesForProfile(String profileId) {
        return repository.findAllByAuthor(profileId, sort).stream()
                .filter(e -> e.getDeletedAt() == null)
                .map(Post::getLikesCount)
                .filter(Objects::nonNull)
                .reduce(Long::sum)
                .orElse(0L);
    }

    public Long countForProfile(String profileId) {
        return repository.countAllByAuthor(profileId);
    }

    public List<PostViewModel> searchContent(String searchKey, String loggedInUserProfileId) {
        List<Post> searchResult = repository.findAllByContentLikeIgnoreCase(searchKey, sort).stream()
                .filter(e -> e.getDeletedAt() == null)
                .filter(e -> !StringUtils.hasText(e.getParentPostId()))
                .collect(Collectors.toList());

        return hydratePosts(searchResult, loggedInUserProfileId);
    }
}
