package com.tofunmi.mitri.webservice.chat;

import com.tofunmi.mitri.usermanagement.profile.ProfileService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created By tofunmi on 31/01/2023
 */
@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ProfileService profileService;

    public ChatService(ChatRepository chatRepository,
                       ChatMessageRepository chatMessageRepository,
                       ProfileService profileService) {
        this.chatRepository = chatRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.profileService = profileService;
    }

    public String newChat(String requester, String[] invitees) {
        final Set<String> participants = new HashSet<>();

        profileService.validateProfileExistence(requester);
        participants.add(requester);

        Assert.notEmpty(invitees, "At least one person needs to be invited to start a chat");
        for (String invitee : invitees) {
            profileService.validateProfileExistence(invitee);
            participants.add(invitee);
        }

        final Instant now = Instant.now();
        final Chat chat = new Chat();
        chat.setCreator(requester);
        chat.setCreatedOn(now);
        chat.setParticipants(participants);
        chat.setLastActivityDate(now);

        return chatRepository.save(chat).getId();
    }

    public List<Chat> getForParticipant(String profileId) {
        return chatRepository.findAllByParticipantsContainsOrderByLastActivityDateDesc(profileId);
    }
}
