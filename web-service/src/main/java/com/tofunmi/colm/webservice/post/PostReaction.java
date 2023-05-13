package com.tofunmi.colm.webservice.post;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created By tofunmi on 15/07/2022
 */
@Data
@Document("post_reactions")
public class PostReaction {
    @Id
    private String id;
    private String postId;
    private String profileId;
    private Reaction reaction;
}
