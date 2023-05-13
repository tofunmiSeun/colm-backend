package com.tofunmi.colm.webservice.mediacontent;

import org.springframework.web.bind.annotation.*;

/**
 * Created By tofunmi on 15/07/2022
 */
@RestController
@RequestMapping("api/media-content")
public class MediaContentController {
    private final MediaContentService mediaContentService;

    public MediaContentController(MediaContentService mediaContentService) {
        this.mediaContentService = mediaContentService;
    }

    @GetMapping("{id}/raw-data")
    public byte[] getMediaContent(@PathVariable("id") String id) {
        return mediaContentService.getForId(id);
    }
}
