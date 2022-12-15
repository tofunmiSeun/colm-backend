package com.tofunmi.mitri.webservice.discover;

import com.tofunmi.mitri.usermanagement.profile.ProfileViewModel;
import com.tofunmi.mitri.webservice.post.PostViewModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created By tofunmi on 15/07/2022
 */
@RestController
@RequestMapping("discover")
public class DiscoverController {
        private final DiscoverService discoverService;

    public DiscoverController(DiscoverService discoverService) {
        this.discoverService = discoverService;
    }

    @GetMapping("top-posts")
    public List<PostViewModel> getTopPosts(@RequestParam String profileId) {
        return discoverService.topPosts(profileId);
    }

    @GetMapping("top-active-profiles")
    public List<ProfileViewModel> getTopActiveProfiles(@RequestParam String profileId) {
        return discoverService.topActiveProfiles(profileId);
    }
}
