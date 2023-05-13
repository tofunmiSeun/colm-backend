package com.tofunmi.colm.webservice.follows;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created By tofunmi on 15/12/2022
 */
@Data
@AllArgsConstructor
public class ProfileFollowedEvent {
    private String follower;
    private String followed;
}
