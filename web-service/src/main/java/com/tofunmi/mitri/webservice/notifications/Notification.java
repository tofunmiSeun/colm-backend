package com.tofunmi.mitri.webservice.notifications;

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
@Document("notifications")
public class Notification {
    @Id
    private String id;
    private String profileId;
    private String actor;
    private NotificationType type;
    @CreatedDate
    private Instant happenedOn;
    private Boolean recipientHasBeenNotified;

    // Metadata about the different notifications currently supported
    // How these are used can be seen in where the different interactions are created
    private String postId;
    private String replyId;
    private Reaction reaction;

    public Notification(String actor, String profileId, NotificationType type) {
        this.actor = actor;
        this.profileId = profileId;
        this.type = type;
        this.happenedOn = Instant.now();
        this.recipientHasBeenNotified = false;
    }
}
