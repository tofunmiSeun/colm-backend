package com.tofunmi.mitri.webservice.notifications;

import com.tofunmi.mitri.usermanagement.profile.ProfileService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Created By tofunmi on 15/07/2022
 */
@RestController
@RequestMapping("notification")
public class NotificationsController {
    private final NotificationService notificationService;
    private final ProfileService profileService;

    public NotificationsController(NotificationService notificationService, ProfileService profileService) {
        this.notificationService = notificationService;
        this.profileService = profileService;
    }

    @GetMapping
    public List<NotificationViewModel> getNotifications(@RequestParam String profileId, Principal principal) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        return notificationService.getNotificationsForProfile(profileId);
    }

    @PostMapping("{id}")
    public void markAsNotified(@PathVariable String id, @RequestParam String profileId, Principal principal) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        notificationService.markAsNotified(id);
    }
}
