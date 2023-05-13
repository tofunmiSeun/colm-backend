package com.tofunmi.colm.api.chat;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Set;

/**
 * Created By tofunmi on 31/01/2023
 */
@Data
@Document("chats")
public class Chat {
    @Id
    private String id;
    private String creator;
    @CreatedDate
    private Instant createdOn;
    private Set<String> participants;
    private Instant lastActivityDate;
    private String lastActivityId;
}
