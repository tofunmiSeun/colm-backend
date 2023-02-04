package com.tofunmi.mitri.webservice.chat;

import com.tofunmi.mitri.usermanagement.profile.ProfileService;
import com.tofunmi.mitri.webservice.mediacontent.MediaContentService;
import com.tofunmi.mitri.webservice.mediacontent.SavedMediaContent;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.*;

/**
 * Created By tofunmi on 31/01/2023
 */
@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ProfileService profileService;
    private final MediaContentService mediaContentService;

    public ChatService(ChatRepository chatRepository,
                       ChatMessageRepository chatMessageRepository,
                       ProfileService profileService,
                       MediaContentService mediaContentService) {
        this.chatRepository = chatRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.profileService = profileService;
        this.mediaContentService = mediaContentService;
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

    public void newMessage(String chatId, String profileId, String text, MultipartFile[] mediaContents) {
        final Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException("Could not find chat for id: " + chatId));
        Assert.isTrue(chat.getParticipants().contains(profileId), "Profile needs to be a participant of this chat to send a message");

        boolean messageContainsContent = (mediaContents != null && mediaContents.length > 0) ||
                StringUtils.hasText(text);
        Assert.isTrue(messageContainsContent, "At least some text or one media content is required");

        final Instant now = Instant.now();
        final ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatId(chatId);
        chatMessage.setSender(profileId);
        chatMessage.setSentOn(now);
        chatMessage.setTextContent(text);

        List<SavedMediaContent> savedMediaContents = mediaContentService.save(mediaContents);
        if (savedMediaContents.size() > 0) {
            chatMessage.setMediaContents(savedMediaContents);
        }

        chatMessage.setSeenBy(Collections.singleton(profileId));
        chatMessageRepository.save(chatMessage);

        chat.setLastActivityDate(now);
        chatRepository.save(chat);
    }
}
