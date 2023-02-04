package com.tofunmi.mitri.webservice.user;

import com.tofunmi.mitri.usermanagement.profile.ProfileService;
import com.tofunmi.mitri.usermanagement.profile.ProfileViewModel;
import com.tofunmi.mitri.webservice.follows.FollowsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Created By tofunmi on 16/07/2022
 */
@RestController
@RequestMapping("profile")
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

    @PostMapping("search/username")
    public List<ProfileViewModel> searchByUsername(@PathVariable String searchTerm) {
        return profileService.searchUsername(searchTerm);
    }

    @PostMapping
    public String createProfile(@RequestParam String username, Principal principal) {
        return profileService.createProfile(principal.getName(), username);
    }

    @GetMapping("{id}/overview")
    public ProfileOverview getProfile(@PathVariable String id, @RequestParam String profileId) {
        return profileOverviewService.getOverview(id, profileId);
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
