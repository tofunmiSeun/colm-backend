package com.tofunmi.mitri.webservice.profile;

import com.tofunmi.mitri.usermanagement.profile.ProfileService;
import com.tofunmi.mitri.usermanagement.profile.ProfileViewModel;
import com.tofunmi.mitri.webservice.follows.FollowsService;
import com.tofunmi.mitri.webservice.post.PostService;
import org.springframework.stereotype.Service;

/**
 * Created By tofunmi on 15/07/2022
 */
@Service
public class ProfileOverviewService {
    private final ProfileService profileService;
    private final PostService postService;
    private final FollowsService followsService;

    public ProfileOverviewService(ProfileService profileService,
                                  PostService postService,
                                  FollowsService followsService) {
        this.profileService = profileService;
        this.postService = postService;
        this.followsService = followsService;
    }

    public ProfileOverview getOverview(String id, String loggedInUserProfileId) {
        ProfileViewModel viewModel = profileService.getProfile(id);

        ProfileOverview overview = new ProfileOverview();
        overview.setId(viewModel.getId());
        overview.setUsername(viewModel.getUsername());
        overview.setName(viewModel.getName());
        overview.setDescription(viewModel.getDescription());

        overview.setPostCount(postService.countForProfile(id));
        overview.setFollowingCount(followsService.countFollowedProfiles(id));
        overview.setFollowersCount(followsService.countFollowers(id));
        overview.setRequesterFollowsProfile(followsService.profileIsBeingFollowed(id, loggedInUserProfileId));
        overview.setLikesCount(postService.countLikesForProfile(id));

        return overview;
    }
}
