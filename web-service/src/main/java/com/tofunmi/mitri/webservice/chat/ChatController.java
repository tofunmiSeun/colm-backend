package com.tofunmi.mitri.webservice.chat;

import com.tofunmi.colm.profile.ProfileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

/**
 * Created By tofunmi on 04/02/2023
 */
@RestController
@RequestMapping("api/chat")
public class ChatController {
    private final ProfileService profileService;
    private final ChatService chatService;

    public ChatController(ProfileService profileService, ChatService chatService) {
        this.profileService = profileService;
        this.chatService = chatService;
    }

    @PostMapping
    public ChatViewModel createChat(@RequestParam String profileId, Principal principal,
                                    @RequestParam(value = "invitees") String[] invitees) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        return chatService.newChat(profileId, invitees);
    }

    @GetMapping
    public List<ChatViewModel> getForProfile(@RequestParam String profileId, Principal principal) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        return chatService.getForParticipant(profileId);
    }

    @DeleteMapping(value = "{id}/leave")
    public void leaveChat(@PathVariable String id, @RequestParam String profileId, Principal principal) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        chatService.leaveChat(id, profileId);
    }

    @PostMapping(value = "{id}/message", consumes = "multipart/form-data")
    public void createChatMessage(@PathVariable String id, @RequestParam String profileId, Principal principal,
                                  @RequestParam(value = "files", required = false) MultipartFile[] files,
                                  @RequestParam(value = "text", required = false) String text) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        chatService.newMessage(id, profileId, text, files);
    }

    @GetMapping(value = "{id}/message")
    public List<ChatMessage> getMessagesInChat(@PathVariable String id) {
        return chatService.getMessagesInChat(id);
    }
}
