package com.tofunmi.colm.api.follows;

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