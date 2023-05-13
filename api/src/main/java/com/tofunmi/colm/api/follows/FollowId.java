package com.tofunmi.colm.api.follows;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created By tofunmi on 15/12/2022
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FollowId {
    private String followedProfileId;
    private String followerProfileId;
}
