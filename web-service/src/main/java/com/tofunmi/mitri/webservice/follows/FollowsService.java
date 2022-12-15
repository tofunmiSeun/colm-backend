package com.tofunmi.mitri.webservice.follows;

import com.tofunmi.mitri.usermanagement.profile.ProfileService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

/**
 * Created By tofunmi on 15/12/2022
 */

@Service
public class FollowsService {
    private final FollowRepository repository;
    private final ProfileService profileService;

    public FollowsService(FollowRepository repository, ProfileService profileService) {
        this.repository = repository;
        this.profileService = profileService;
    }

    public void followProfile(String profileToFollow, String followerProfileId) {
        profileService.validateProfileExistence(profileToFollow);
        profileService.validateProfileExistence(followerProfileId);

        if (Objects.equals(profileToFollow, followerProfileId)) {
            throw new IllegalArgumentException("A profile cannot follow itself");
        }

        FollowId followId = new FollowId(profileToFollow, followerProfileId);
        if (repository.existsById(followId)) {
            return;
        }

        Follow follow = new Follow();
        follow.setFollowId(followId);
        follow.setFollowedOn(Instant.now());
        repository.save(follow);
    }

    public void unfollowProfile(String profileToUnfollow, String followerProfileId) {
        profileService.validateProfileExistence(profileToUnfollow);
        profileService.validateProfileExistence(followerProfileId);

        FollowId followId = new FollowId(profileToUnfollow, followerProfileId);
        repository.deleteById(followId);
    }

    public boolean profileIsBeingFollowed(String followedProfile, String followerProfileId) {
        profileService.validateProfileExistence(followedProfile);
        profileService.validateProfileExistence(followerProfileId);

        FollowId followId = new FollowId(followedProfile, followerProfileId);
        return repository.existsById(followId);
    }

    public Long countFollowers(String profileId) {
        profileService.validateProfileExistence(profileId);

        FollowId followId = new FollowId(profileId, null);
        Follow follow = new Follow();
        follow.setFollowId(followId);

        return repository.count(Example.of(follow));
    }

    public Long countFollowedProfiles(String profileId) {
        profileService.validateProfileExistence(profileId);

        FollowId followId = new FollowId(null, profileId);
        Follow follow = new Follow();
        follow.setFollowId(followId);

        return repository.count(Example.of(follow));
    }
}
