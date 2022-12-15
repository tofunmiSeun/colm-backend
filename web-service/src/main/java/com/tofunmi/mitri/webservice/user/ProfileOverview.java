package com.tofunmi.mitri.webservice.user;

import lombok.Data;

/**
 * Created By tofunmi on 16/07/2022
 */
@Data
public class ProfileOverview {
    private String id;
    private String username;
    private String name;
    private String description;
    private Long postCount;
    private Long followingCount;
    private Long followersCount;
    private Long likesCount;
    private Boolean requesterFollowsProfile;
}
