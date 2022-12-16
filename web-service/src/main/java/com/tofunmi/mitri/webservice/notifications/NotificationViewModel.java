package com.tofunmi.mitri.webservice.notifications;

import com.tofunmi.mitri.webservice.post.Reaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

/**
 * Created By tofunmi on 15/12/2022
 */
@Data
@AllArgsConstructor
public class NotificationViewModel {
    private String id;
    private Instant happenedOn;
    private String actor;
    private String actorUsername;
    private NotificationType type;
    private Boolean recipientHasBeenNotified;
    private String postId;
    private String replyId;
    private Reaction reaction;
}
