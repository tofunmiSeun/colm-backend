package com.tofunmi.mitri.usermanagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created By tofunmi on 12/07/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSetupResponsePair {
    private String sessionId;
    private UserSetupResponse response;
}
