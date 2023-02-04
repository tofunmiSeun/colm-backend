package com.tofunmi.mitri.webservice.chat;

import com.tofunmi.mitri.webservice.mediacontent.SavedMediaContent;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@Document("chat_messages")
public class ChatMessage {
    @Id
    private String id;
    private String chatId;
    private String sender;
    private Instant sentOn;
    private String textContent;
    private List<SavedMediaContent> mediaContents;
    private Set<String> seenBy;
}
