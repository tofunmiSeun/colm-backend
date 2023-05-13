package com.tofunmi.colm.profile;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created By tofunmi on 12/07/2022
 */

@Data
@Document("profiles")
@NoArgsConstructor
public class Profile {
    @Id
    private String id;
    private String userId;
    private String username;
    private String name;
    private String description;

    Profile(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
