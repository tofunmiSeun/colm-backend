package com.tofunmi.mitri.webservice.chat;

import com.tofunmi.colm.profile.ProfileViewModel;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Data
public class ChatViewModel {
    @Id
    private String id;
    private ProfileViewModel creator;
    private Long createdOnMilliseconds;
    private Set<ProfileViewModel> participants;
    private Long lastActivityTimeMilliseconds;
    private String lastMessageContent;
}
