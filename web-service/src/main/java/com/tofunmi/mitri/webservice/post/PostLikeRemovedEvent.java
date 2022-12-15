package com.tofunmi.mitri.webservice.post;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created By tofunmi on 15/12/2022
 */
@Data
@AllArgsConstructor
public class PostLikeRemovedEvent {
    private String profileThatLikedPost;
    private String originalPostAuthor;
    private String postId;
}
