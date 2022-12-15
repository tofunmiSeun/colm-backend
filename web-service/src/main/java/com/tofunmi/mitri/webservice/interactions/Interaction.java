package com.tofunmi.mitri.webservice.interactions;

import com.tofunmi.mitri.webservice.post.Reaction;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Created By tofunmi on 15/12/2022
 */
@Data
@NoArgsConstructor
@Document("interaction")
public class Interaction {
    @Id
    private String id;
    private String actor;
    private String recipient;
    private InteractionType type;
    @CreatedDate
    private Instant createdOn;
    private Boolean recipientHasBeenNotified;

    // Metadata about the different interactions currently supported
    // How these are used can be seen in where the different interactions are created
    private String postId;
    private String replyId;
    private Reaction reaction;

    public Interaction(String actor, String recipient, InteractionType type) {
        this.actor = actor;
        this.recipient = recipient;
        this.type = type;
        this.createdOn = Instant.now();
        this.recipientHasBeenNotified = false;
    }
}
