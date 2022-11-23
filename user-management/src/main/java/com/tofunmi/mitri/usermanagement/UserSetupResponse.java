package com.tofunmi.mitri.usermanagement;

import com.tofunmi.mitri.usermanagement.profile.ProfileOverview;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created By tofunmi on 12/07/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSetupResponse {
    private String userId;
    private String email;
    private String name;
    private Boolean isExistingUser;
    private List<ProfileOverview> profiles;
}
