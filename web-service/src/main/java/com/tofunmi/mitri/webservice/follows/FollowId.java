package com.tofunmi.mitri.webservice.follows;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Created By tofunmi on 15/12/2022
 */
@AllArgsConstructor
@NoArgsConstructor
public class FollowId {
    private String followedProfileId;
    private String followerProfileId;
}
