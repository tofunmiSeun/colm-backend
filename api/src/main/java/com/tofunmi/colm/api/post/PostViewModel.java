package com.tofunmi.colm.api.post;

import com.tofunmi.colm.api.mediacontent.SavedMediaContent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created By tofunmi on 15/07/2022
 */
@Data
@AllArgsConstructor
public class PostViewModel {
    private String id;
    private String author;
    private Long createdAtMilliseconds;
    private String content;
    private List<SavedMediaContent> mediaContents;
    private String authorUsername;
    private String authorName;
    private Boolean likedByProfile;
    private Long likes;
}
