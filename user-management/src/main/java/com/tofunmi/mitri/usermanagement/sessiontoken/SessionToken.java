package com.tofunmi.mitri.usermanagement.sessiontoken;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created By tofunmi on 12/07/2022
 */

@Document("session_tokens")
@Data
@NoArgsConstructor
public class SessionToken {
    @Id
    private String token;
    private String userId;

    SessionToken(String token, String userId) {
        this.token = token;
        this.userId = userId;
    }
}
