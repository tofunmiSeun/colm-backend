package com.tofunmi.mitri.webservice.post;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created By tofunmi on 15/07/2022
 */
@Data
@AllArgsConstructor
public class PostViewModel {
    private String id;
    private String content;
    private String authorUsername;
    private String authorName;
    private Boolean likedByProfile;
}
