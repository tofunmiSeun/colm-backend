package com.tofunmi.mitri.webservice.chat;

import com.tofunmi.mitri.usermanagement.profile.ProfileViewModel;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.Set;

@Data
public class ChatViewModel {
    @Id
    private String id;
    private ProfileViewModel creator;
    @CreatedDate
    private Instant createdOn;
    private Set<ProfileViewModel> participants;
    private Instant lastActivityDate;
}
