package com.tofunmi.mitri.webservice.follows;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created By tofunmi on 15/12/2022
 */
@Data
@AllArgsConstructor
public class ProfileUnfollowedEvent {
    private String follower;
    private String followed;
}