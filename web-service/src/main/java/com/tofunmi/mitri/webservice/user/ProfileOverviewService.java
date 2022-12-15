package com.tofunmi.mitri.webservice.user;

import com.tofunmi.mitri.usermanagement.profile.ProfileService;
import com.tofunmi.mitri.usermanagement.profile.ProfileViewModel;
import com.tofunmi.mitri.webservice.follows.FollowService;
import com.tofunmi.mitri.webservice.post.PostService;
import org.springframework.stereotype.Service;

/**
 * Created By tofunmi on 15/07/2022
 */
@Service
public class ProfileOverviewService {
    private final ProfileService profileService;
    private final PostService postService;
    private final FollowService followService;

    public ProfileOverviewService(ProfileService profileService,
                                  PostService postService,
                                  FollowService followService) {
        this.profileService = profileService;
        this.postService = postService;
        this.followService = followService;
    }

    public ProfileOverview getOverview(String id, String loggedInUserProfileId) {
        ProfileViewModel viewModel = profileService.getProfile(id);

        ProfileOverview overview = new ProfileOverview();
        overview.setId(viewModel.getId());
        overview.setUsername(viewModel.getUsername());
        overview.setName(viewModel.getName());
        overview.setDescription(viewModel.getDescription());

        overview.setPostCount(postService.countForProfile(id));
        overview.setFollowingCount(followService.countFollowedProfiles(id));
        overview.setFollowersCount(followService.countFollowers(id));
        overview.setRequesterFollowsProfile(followService.profileIsBeingFollowed(id, loggedInUserProfileId));
        overview.setLikesCount(postService.countLikesForProfile(id));

        return overview;
    }
}
