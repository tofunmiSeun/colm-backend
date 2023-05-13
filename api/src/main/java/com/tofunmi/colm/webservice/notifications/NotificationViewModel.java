package com.tofunmi.colm.webservice.notifications;

import com.tofunmi.colm.webservice.post.Reaction;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created By tofunmi on 15/12/2022
 */
@Data
@AllArgsConstructor
public class NotificationViewModel {
    private String id;
    private Long happenedOnMilliseconds;
    private String actor;
    private String actorUsername;
    private NotificationType type;
    private Boolean recipientHasBeenNotified;
    private String postId;
    private String replyId;
    private Reaction reaction;
}
