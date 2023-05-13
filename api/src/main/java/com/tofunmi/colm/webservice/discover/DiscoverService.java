package com.tofunmi.colm.webservice.discover;

import com.tofunmi.colm.profile.ProfileRepository;
import com.tofunmi.colm.profile.ProfileService;
import com.tofunmi.colm.profile.ProfileViewModel;
import com.tofunmi.colm.webservice.follows.FollowsService;
import com.tofunmi.colm.webservice.post.Post;
import com.tofunmi.colm.webservice.post.PostService;
import com.tofunmi.colm.webservice.post.PostRepository;
import com.tofunmi.colm.webservice.post.PostViewModel;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created By tofunmi on 15/12/2022
 */
@Service
public class DiscoverService {
    private final PostService postService;
    private final ProfileService profileService;
    private final FollowsService followsService;
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;

    public DiscoverService(PostService postService, ProfileService profileService,
                           FollowsService followsService, PostRepository postRepository,
                           ProfileRepository profileRepository) {
        this.postService = postService;
        this.profileService = profileService;
        this.followsService = followsService;
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
    }

    public List<PostViewModel> topPosts(String profileId) {
        Set<String> followedProfiles = followsService.getFollowedProfiles(profileId);
        followedProfiles.add(profileId);

        List<Post> posts = postRepository.findAll(Sort.by("createdOn").descending()).stream()
                .filter(e -> e.getDeletedAt() == null)
                .filter(e -> !StringUtils.hasText(e.getParentPostId()))
                .filter(e -> !followedProfiles.contains(e.getAuthor()))
                .collect(Collectors.toList());

        return postService.hydratePosts(posts, profileId);
    }

    public List<ProfileViewModel> topActiveProfiles(String profileId) {
        Set<String> followedProfiles = followsService.getFollowedProfiles(profileId);
        followedProfiles.add(profileId);

        return profileRepository.findAll().stream()
                .filter(e -> !followedProfiles.contains(e.getId()))
                .map(profileService::mapToViewModel)
                .collect(Collectors.toList());
    }
}
