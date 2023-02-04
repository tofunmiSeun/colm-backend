package com.tofunmi.mitri.webservice.chat;

import com.tofunmi.mitri.usermanagement.profile.ProfileService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Created By tofunmi on 15/07/2022
 */
@RestController
@RequestMapping("chat")
public class ChatController {
    private final ProfileService profileService;
    private final ChatService chatService;

    public ChatController(ProfileService profileService, ChatService chatService) {
        this.profileService = profileService;
        this.chatService = chatService;
    }

    @PostMapping
    public String createChat(@RequestParam String profileId, Principal principal,
                             @RequestParam(value = "invitees") String[] invitees) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        return chatService.newChat(profileId, invitees);
    }

    @GetMapping
    public List<Chat> getForProfile(@RequestParam String profileId, Principal principal) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        return chatService.getForParticipant(profileId);
    }
}
