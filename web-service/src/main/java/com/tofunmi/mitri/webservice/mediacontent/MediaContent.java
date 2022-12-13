package com.tofunmi.mitri.webservice.mediacontent;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created By tofunmi on 12/12/2022
 */
@Data
@Document("media_contents")
public class MediaContent {
    @Id
    private String id;
    private String mimeType;
    private String extension;
    private String fileName;
    private byte[] content;
}
