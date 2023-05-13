package com.tofunmi.colm;

import com.tofunmi.colm.profile.ProfileViewModel;
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
    private List<ProfileViewModel> profiles;
}
