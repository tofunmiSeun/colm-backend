package com.tofunmi.mitri.webservice.interactions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

/**
 * Created By tofunmi on 15/12/2022
 */
@Data
@AllArgsConstructor
public class InteractionViewModel {
    private String id;
    private Instant createdOn;
    private String actor;
    private Boolean recipientHasBeenNotified;
    private String postId;
    private String replyId;
    private String description;
}
