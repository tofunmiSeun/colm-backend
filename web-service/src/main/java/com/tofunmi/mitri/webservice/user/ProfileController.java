package com.tofunmi.mitri.webservice.user;

import com.tofunmi.mitri.usermanagement.profile.ProfileService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created By tofunmi on 16/07/2022
 */
@RestController
@RequestMapping("profile")
public class ProfileController {
    private final ProfileService profileService;
    private final ProfileOverviewService profileOverviewService;

    public ProfileController(ProfileService profileService, ProfileOverviewService profileOverviewService) {
        this.profileService = profileService;
        this.profileOverviewService = profileOverviewService;
    }

    @GetMapping("available")
    public Boolean isUsernameAvailable(@RequestParam String username) {
        return profileService.isUsernameAvailable(username);
    }

    @PostMapping
    public String createProfile(@RequestParam String username, Principal principal) {
        return profileService.createProfile(principal.getName(), username);
    }

    @GetMapping("{id}/overview")
    public ProfileOverview getProfile(@PathVariable String id) {
        return profileOverviewService.getOverview(id);
    }
}
