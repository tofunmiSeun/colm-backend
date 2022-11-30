package com.tofunmi.mitri.usermanagement.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created By tofunmi on 12/07/2022
 */

@Document("users")
@Data
@NoArgsConstructor
public class User {
    @Id
    private String id;
    private String email;
    private String fullName;
    private String firstName;

    public User(String email, String fullName, String firstName) {
        this.email = email;
        this.fullName = fullName;
        this.firstName = firstName;
    }
}
