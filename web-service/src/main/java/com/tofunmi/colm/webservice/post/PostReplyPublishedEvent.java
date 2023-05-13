package com.tofunmi.colm.webservice.post;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created By tofunmi on 15/12/2022
 */
@Data
@AllArgsConstructor
public class PostReplyPublishedEvent {
    private String replyId;
    private String replyAuthor;
    private String originalPostId;
    private String originalPostAuthor;
}
