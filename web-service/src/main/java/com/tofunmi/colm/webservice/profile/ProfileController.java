package com.tofunmi.colm.webservice.profile;

import com.tofunmi.colm.profile.ProfileService;
import com.tofunmi.colm.profile.ProfileViewModel;
import com.tofunmi.colm.webservice.follows.FollowsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Created By tofunmi on 16/07/2022
 */
@RestController
@RequestMapping("api/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final ProfileOverviewService profileOverviewService;
    private final FollowsService followsService;

    public ProfileController(ProfileService profileService,
                             ProfileOverviewService profileOverviewService,
                             FollowsService followsService) {
        this.profileService = profileService;
        this.profileOverviewService = profileOverviewService;
        this.followsService = followsService;
    }

    @GetMapping("available")
    public Boolean isUsernameAvailable(@RequestParam String username) {
        return profileService.isUsernameAvailable(username);
    }

    @GetMapping("search/username")
    public List<ProfileViewModel> searchByUsername(@RequestParam String key) {
        return profileService.searchUsername(key);
    }

    @PostMapping
    public String createProfile(@RequestParam String username, Principal principal) {
        return profileService.createProfile(principal.getName(), username);
    }

    @GetMapping("{id}/overview")
    public ProfileOverview getProfile(@PathVariable String id, @RequestParam String profileId) {
        return profileOverviewService.getOverview(id, profileId);
    }

    @GetMapping("followers")
    public List<ProfileViewModel> getFollowers(@RequestParam String profileId, Principal principal) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        return profileService.getProfiles(followsService.getFollowersProfiles(profileId));
    }

    @GetMapping("followed")
    public List<ProfileViewModel> getFollowed(@RequestParam String profileId, Principal principal) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        return profileService.getProfiles(followsService.getFollowedProfiles(profileId));
    }

    @PostMapping("{id}/follow")
    public void follow(@PathVariable String id, @RequestParam String profileId, Principal principal) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        followsService.followProfile(id, profileId);
    }

    @PostMapping("{id}/unfollow")
    public void unfollow(@PathVariable String id, @RequestParam String profileId, Principal principal) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        followsService.unfollowProfile(id, profileId);
    }
}
