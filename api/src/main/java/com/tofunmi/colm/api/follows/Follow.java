package com.tofunmi.colm.api.follows;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Created By tofunmi on 15/12/2022
 */
@Data
@Document("follow")
public class Follow {
    @Id
    FollowId followId;
    @CreatedDate
    private Instant followedOn;
}
