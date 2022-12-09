package com.tofunmi.mitri.webservice.post;

import lombok.Data;

/**
 * Created By tofunmi on 15/07/2022
 */
@Data
public class CreatePostRequest {
    private String content;
    private String parentPostId;
}
