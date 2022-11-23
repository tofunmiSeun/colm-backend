package com.tofunmi.mitri.webservice.post;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Created By tofunmi on 15/07/2022
 */
@Data
@Document("posts")
public class Post {
    @Id
    private String id;
    private String content;
    private String author;
    @CreatedDate
    private Instant createdOn;
}